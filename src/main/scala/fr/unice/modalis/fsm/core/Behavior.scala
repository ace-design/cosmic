package fr.unice.modalis.fsm.core

import fr.unice.modalis.fsm.exceptions.NodeNotFoundException
import fr.unice.modalis.fsm.condition.{TrueCondition, TickCondition}
import fr.unice.modalis.fsm.actions.StateAction
import scala.collection.mutable.ArrayBuffer

/**
 * A user's behavior
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
	 * Delete a node (+ transitions from/to this node)
	 * @param node node to be deleted
	 * @return A new behavior with the node deleted
	 */
	def deleteNode(node: Node):Behavior = 
	{	
		// Transitions to be kept 
		val newTransitions = transitions.filter(a => !(a.source==node) && !(a.destination.name==node))
		new Behavior(entry, nodes - node, newTransitions)
	}

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
		if(nodes.contains(transition.source) && nodes.contains(transition.destination))
			new Behavior(entryPoint, nodes, transitions + transition)
		else
			throw new NodeNotFoundException()
	}
	
	/**
	 * Delete a transition link
	 * @param  transition transition to be deleted
	 * @return  A new behavior with the transition deleted
	 */
	def deleteTransition(transition: Transition):Behavior =
	{
	  new Behavior(entryPoint, nodes, transitions - transition)
	}

  /**
   * Get the node accessed at time t
   * @param t Time
   * @return Node accessed at time t
   */
  def nodeAt(t:Int):Node = {
    var currentNode:Node = this.entryPoint
    for (i <- 0 to t)
    {
      val possibleTransition:Transition = transitions.filter(x => x.source.equals(currentNode)).head
      possibleTransition.condition match {
        case TickCondition(n) => if (i%n ==0) currentNode = possibleTransition.destination
        case TrueCondition() => currentNode = possibleTransition.destination
        case _ => /* NOP */
      }
    }
    currentNode
  }


	override def toString():String = "FSM: Nodes=" + nodes + " Transitions=" + transitions

}