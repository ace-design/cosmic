package fr.unice.modalis.fsm.algo

import fr.unice.modalis.fsm.core._
import scala.collection.mutable.ArrayBuffer
import fr.unice.modalis.fsm.vm._
import fr.unice.modalis.fsm.condition.TickCondition


/**
 * This object contains algorithms used to transform behaviors
 */
object Transformation {

  def compose(b1:SimpleTemporalBehavior, b2:SimpleTemporalBehavior):Behavior =
  {
    val setActions = ArrayBuffer[Action]()

    val composedPeriod = Utils.lcm(b1.tickPeriod, b2.tickPeriod)
    val blankAutomata = Utils.generateDevelopedTemporalBlankAutomata(composedPeriod)

    for (i <- 1 to composedPeriod) {
      if (i % (b1.tickPeriod) == 0)
        setActions += new AddActions(blankAutomata.nodeAt(i-1), b1.actionNode.actions)

      if (i % (b2.tickPeriod) == 0)
        setActions += new AddActions(blankAutomata.nodeAt(i-1), b2.actionNode.actions)
    }

    val actions = setActions.toList
    println(actions)
    VirtualMachine.apply(blankAutomata, actions)


  }
  /**
   * Factorize a behavior
   * @param b Behavior
   * @return An actions list to factorize the behavior
   */
  def factorize(b:Behavior):List[Action] = {
    factorize_int(b, b.entryPoint, b.entryPoint, ArrayBuffer[Action](), 0)

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
  private def factorize_int(behavior:Behavior, currentNode:Node, origin:Node, actions:ArrayBuffer[Action], counter:Int):List[Action]=
  {

    // STEP 0 : Identify transition
    val transition:Transition = behavior.transitions.filter(t => t.source.equals(currentNode)).head

    // STEP 1a : If transition destination == entrypoint : finish
    if (transition.destination.name.equals(behavior.entryPoint.name)){
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
	def develop(b: Behavior):List[Action] = {
    val setActions = ArrayBuffer[Action]()
    b.transitions.foreach(t => setActions ++= develop_int(t))
    setActions.toList
  }

  /**
   * Develop a transition
   * @param t Transition
   * @return An actions list to develop the transition
   */
	private def develop_int(t: Transition):List[Action] = {
	  
	  val currentSource: Node = t.source
	  val currentDestination: Node = t.destination
	  val actions = new ArrayBuffer[Action]
	  
	 
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
	  
}