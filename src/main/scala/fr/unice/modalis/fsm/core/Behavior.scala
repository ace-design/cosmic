package fr.unice.modalis.fsm.core

import fr.unice.modalis.fsm.exceptions.{NotAnIDLENodeException, NodeNotFoundException}

/**
 * A user's behavior
 * @constructor create a behavior with a given entry node
 * @constructor create a behavior with a given entry node, a set of nodes and a set of transitions
 */
class Behavior (entry:Node, nodesSet:Set[Node], transitionSet:Set[Transition]) {

	def this(entry:Node) = this(entry, Set[Node](entry), Set[Transition]())

	val nodes = nodesSet
	val transitions = transitionSet

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
		val newTransitions = transitions.filter(a => !a.source.name.equals(node.name) && !a.destination.name.equals(node.name))
		new Behavior(entry, nodes - node, newTransitions)
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
	
	override def toString():String = "FSM: Nodes=" + nodes + " Transitions=" + transitions

}