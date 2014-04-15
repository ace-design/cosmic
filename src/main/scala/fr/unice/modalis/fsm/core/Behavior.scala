package fr.unice.modalis.fsm.core

import fr.unice.modalis.fsm.exceptions.NodeNotFoundException
import fr.unice.modalis.fsm.condition.{TrueCondition, TickCondition}
import fr.unice.modalis.fsm.actions.StateAction
import fr.unice.modalis.fsm.vm.VirtualMachine
import fr.unice.modalis.fsm.algo.Transformation

/**
 * A user behavior
 * @constructor create a behavior with a given entry node
 * @constructor create a behavior with a given entry node, a set of nodes and a set of transitions
 */
class Behavior (entry:Node, nodesSet:Set[Node], transitionSet:Set[Transition]) {

  def this(entry:Node) = this(entry, Set[Node](entry), Set[Transition]())

  val nodes:Set[Node] = nodesSet
  val transitions:Set[Transition] = transitionSet

  val entryPoint:Node = entry 	// FSM Entry point


  /**
   * Add a node
   * @param node node to add
   * @return A new behavior with the node added
   *
   */
  def addNode(node: Node):Behavior = new Behavior(entry, nodes + node, transitions)

  /**
   * Add nodes
   * @param nodesL Nodes list
   * @return A new behavior with the nodes added
   */
  def addNodes(nodesL: List[Node]) = {
    var processedBehavior:Behavior = this
    nodesL.foreach(n => processedBehavior = processedBehavior.addNode(n))
    processedBehavior
  }

  /**
   * Delete a node
   * @param node node to be deleted
   * @return A new behavior with the node deleted
   */
  def deleteNode(node: Node):Behavior =
  {
    new Behavior(entry, nodes - node, transitions)
  }

  /**
   * Add an action to a referenced node
   * @param node Node
   * @param action Action to add
   */
  def addAction(node: Node, action:StateAction):Unit = {
    node.addAction(action)
  }

  /**
   * Add a transition link
   * @param transition transition between two nodes
   * @return A new behavior with the transition added
   *
   */
  def addTransition(transition: Transition):Behavior =
  {
    if (nodes.filter(p => p.name == transition.source.name).size == 0)
      throw new NodeNotFoundException(transition.source)
    if (nodes.filter(p => p.name == transition.destination.name).size == 0)
      throw new NodeNotFoundException(transition.destination)

    new Behavior(entryPoint, nodes, transitions + transition)

  }

  /**
   * Add transition links
   * @param transitionsL transitions list
   * @return A new behavior with the transitions added
   */
  def addTransitions(transitionsL: List[Transition]):Behavior =
  {
    var processedBehavior:Behavior = this
    transitionsL.foreach(t => processedBehavior = processedBehavior.addTransition(t))
    processedBehavior
  }

  /**
   * Delete a transition link
   * @param  transition transition to be deleted
   * @return  A new behavior with the transition deleted
   */
  def deleteTransition(transition: Transition):Behavior =
  {
    val newSet = transitions.filterNot(p => (p.source.name == transition.source.name) && (p.destination.name == transition.destination.name) && (p.condition.equals(transition.condition)))
    new Behavior(entryPoint, nodes, newSet)
  }

  /**
   * Get the node accessed at tick t
   * @param t tick
   * @return Node accessed at tick t
   */
  def nodeAt(t:Int):Node = {
    var currentNode:Node = this.entryPoint
    for (i <- 1 to t)
    {
      val possibleTransition:Set[Transition] = transitions.filter(x => x.source.equals(currentNode))
      possibleTransition.foreach(t => f(t))

      def f(t:Transition) = {
        t.condition match {
          case TickCondition(n) => if (i % n == 0) currentNode = t.destination
          case TrueCondition() => currentNode = t.destination
          case _ => /* NOP */
        }
      }

    }
    currentNode
  }

  /**
   * Get the behavior period
   * @return Behavior period
   */
  def period():Int = {
    var i:Int = 1
    while(!VirtualMachine.apply(this, Transformation.develop(this)).nodeAt(i).equals(entryPoint))
      i += 1
    i
  }

  /**
   * Compose behavior
   * @param b Behavior to be composed with
   * @return Composed behavior
   */
  def +(b:Behavior):Behavior = Transformation.compose(this, b)

  override def toString():String = "FSM: Nodes=" + nodes + " Transitions=" + transitions

}