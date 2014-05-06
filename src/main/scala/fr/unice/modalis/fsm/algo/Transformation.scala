package fr.unice.modalis.fsm.algo

import fr.unice.modalis.fsm.core._
import scala.collection.mutable.ArrayBuffer
import fr.unice.modalis.fsm.vm._
import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.converter.actions.compatibility.{Plateform, ActionDispatcher}
import fr.unice.modalis.fsm.actions.flow.SequentialActions


/**
 * This object contains algorithms used to transform behaviors
 */
object Transformation {

  /**
   * Compose two behavior
   * @param b1 Behavior 1
   * @param b2 Behavior 2
   * @return Composed behavior
   */
  def compose(b1:Behavior, b2:Behavior):Behavior =
  {

    val composedPeriod = (b1.period(), b2.period()) match {
      case (0, 0) => 0
      case (0 , a) => a
      case (a, 0) => a
      case (a ,b) => Utils.lcm(a,b)
    }


    val setActions = ArrayBuffer[Instruction]()

    // Build new entry point
    val entry = b1.entryPoint + b2.entryPoint
    setActions += new AddNode(entry)

    // Init : Previous node at start = entry point
    var previousNode:Node = entry

    // Generate new nodes
    for (i<- 1 to composedPeriod-1)
    {
      // Action composition rule : if (index-1)%P1 == 0 => add A ||  if (index-1)%P2 == 0 ==> add B
      def generateNode(index:Int):Node = {
        if (b1.newNodeAt(index) && b2.newNodeAt(index))
          new Node("G"+index, b1.nodeAt(index).actions.union(b2.nodeAt(index).actions))
        else if (b1.newNodeAt(index))
          new Node("G"+index, b1.nodeAt(index).actions)
        else if (b2.newNodeAt(index))
          new Node("G"+index, b2.nodeAt(index).actions)
        else
          new Node("G"+index)
      }


      val newNode = generateNode(i)

      setActions += new AddNode(newNode)
      setActions += new AddTransition(new Transition(previousNode, newNode, new TickCondition(1)))

      // Update previous node with newly created one
      previousNode = newNode

    }


    setActions += new AddTransition(new Transition(previousNode, entry, new TickCondition(1)))

    // Build composed automata
    val composed = VirtualMachine.apply(new Behavior(entry), setActions.toList)

    // Factorize composed automata
    val factorizeList = Transformation.factorize(composed)
    VirtualMachine.apply(composed, factorizeList)
  }
  /**
   * Factorize a behavior
   * @param b Behavior
   * @return An actions list to factorize the behavior
   */
  def factorize(b:Behavior):List[Instruction] = {
    factorize_int(b, b.entryPoint, b.entryPoint, ArrayBuffer[Instruction](), 0)

  }

  /**
   * Internal factorization
   * @param behavior Behavior to factorize
   * @param currentNode Current node
   * @param origin Last significant node (ie != IDLE node)
   * @param actions Current action list
   * @param counter Time counter since last significant node
   * @return An action list to factorize the behavior
   */
  private def factorize_int(behavior:Behavior, currentNode:Node, origin:Node, actions:ArrayBuffer[Instruction], counter:Int):List[Instruction]=
  {

    // STEP 0 : Identify transition
    val transition:Transition = behavior.transitions.filter(t => t.source.equals(currentNode)).head

    // STEP 1a : If transition destination == entrypoint : add the transition and finish
    if (transition.destination.name.equals(behavior.entryPoint.name)){
      actions ++= Array(new DeleteTransition(transition), new AddTransition(new Transition(origin, transition.destination,
        transition.condition match {
          case TickCondition(n) =>
            new TickCondition(counter+n)
        }
      )))
      actions.toList
    } else
    {
      // STEP 2a : Identify transition condition type
      transition.condition match {
        case TickCondition(n) => // STEP 3a: Tick condition : Seem to be factorizable
        {
          if (transition.destination.actions == null || transition.destination.actions.size == 0) // STEP 3a1: IDLE node : factorizable =)
          // STEP 3a2: RECURSE on next node
            factorize_int(behavior, transition.destination, origin, actions ++= Array(new DeleteTransition(transition), new DeleteNode(transition.destination)), counter + n)
          else // STEP 3a2 : Not IDLE node : just loop
            factorize_int(behavior, transition.destination, transition.destination, actions ++= Array(new DeleteTransition(transition), new AddTransition(new Transition(origin, transition.destination, new TickCondition(counter +n)))), 0)
        }

        case _ => // STEP 2b : Other condition : Not factorizable
        {
          if (counter > 0) // STEP 2b1 : Previous nodes have been factorized : create the new transition
            factorize_int(behavior, transition.destination, transition.destination, actions ++= Array(new AddTransition(new Transition(origin, transition.destination, new TickCondition(counter)))), 0)
          else // Step 2b2 : No previous nodes have been factorized
            factorize_int(behavior, transition.destination, transition.destination, actions, counter)
        }
      }
    }
  }

  /**
   * Develop a behavior
   * @param b Behavior
   * @return An actions list to develop the behavior
   */
  def develop(b: Behavior):List[Instruction] = {
    val setActions = ArrayBuffer[Instruction]()
    b.transitions.foreach(t => setActions ++= develop_int(t))
    setActions.toList
  }

  /**
   * Develop a transition
   * @param t Transition
   * @return An actions list to develop the transition
   */
  private def develop_int(t: Transition):List[Instruction] = {

    val currentSource: Node = t.source
    val currentDestination: Node = t.destination
    val actions = new ArrayBuffer[Instruction]


    t.condition match {
      case TickCondition(f) if f>1=>  // Tick condition (freq : t)
      {
        var newNode:Node = null
        var previousNode:Node = null

        for (i <- 1 to f-1) yield { // Generate t-1 nodes
          newNode = new Node(currentSource.name + "D" + i)
          def newTransition =   // Generate transitions between newly created nodes
            if (i==1) new Transition(currentSource, newNode, new TickCondition(1))  // First node case
            else new Transition(previousNode, newNode, new TickCondition(1))

          actions += new AddNode(newNode)
          actions += new AddTransition(newTransition)
          previousNode = newNode
        }
        actions += new AddTransition(new Transition(newNode, currentDestination, new TickCondition(1))) // Last node case
        actions += new DeleteTransition(t)  // Delete non-developed transition

      }
      case _ => /* DO NOTHING */
    }

    actions.toList
  }

  /**
   * Slice a behavior into two distincts automata
   * @param b Behavior
   * @return Two automata sliced according their actions compatibility with final deployement
   */
  def slice(b: Behavior): (Behavior, Behavior) = {
    val period = b.period()
    // Slice actions
    val actionsDispatched = ActionDispatcher(b.entryPoint.actions, Plateform.BOARD) //Get (board, non-board) actions

    // Build new actions flow
    var actionsFlowBoard = new SequentialActions()
    var actionsFlowBridge = new SequentialActions()

    actionsDispatched._1.foreach(a => actionsFlowBoard = actionsFlowBoard.add(a))
    actionsDispatched._2.foreach(a => actionsFlowBridge = actionsFlowBridge.add(a))

    // Build automata
    val boardAutomata = Utils.generateDevelopedTemporalActionAutomata(period, actionsFlowBoard)
    val bridgeAutomata = Utils.generateDevelopedTemporalActionAutomata(period, actionsFlowBridge)

    (boardAutomata, bridgeAutomata)

  }

  /**
   * Forward a behavior
   * @param b behavior
   * @return Forwarded behavior
   */
  def forward(b: Behavior): Behavior = {
    Utils.generateDevelopedTemporalActionAutomata(b.period(), b.entryPoint.actions)
  }

  def minimize(b: Behavior): Behavior = {
    throw new Exception("Not yet implemented")
    b
  }

}