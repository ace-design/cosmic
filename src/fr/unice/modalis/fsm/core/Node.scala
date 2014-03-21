package fr.unice.modalis.fsm.core

import fr.unice.modalis.fsm.actions.Action
import fr.unice.modalis.fsm.core.Node;

/**
 * Node class
 * This class represents a FSM node with actions attached 
 */
class Node(nodeName: String, actionsSet:Set[Action] = null) {
	
	// Node properties
	val name = nodeName
	def actions = if (actionsSet == null) Set[Action]() else actionsSet
	
	/**
	 * Add an action for the current Node
	 * @param action Action to add
	 * @return new 
	 */
	def addAction(action:Action):Node = new Node(name, actionsSet + action)
	
	/**
	 * Print the node's name
	 * @return Node's name
	 */
	override def toString():String = name
}