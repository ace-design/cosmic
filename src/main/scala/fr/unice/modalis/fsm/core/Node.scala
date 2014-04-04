package fr.unice.modalis.fsm.core

import fr.unice.modalis.fsm.actions.StateAction
import scala.collection.mutable.Set
/**
 * Node class
 * This class represents a FSM node with actions attached 
 */
class Node(nodeName: String, actionsSet:Set[StateAction]) {

  def this(nodeName:String) = this(nodeName, Set[StateAction]())

	// Node properties
	val name = nodeName
	val actions = actionsSet
	
	/**
	 * Add an action for the current Node
	 * @param action Action to add
	 */
	def addAction(action:StateAction):Unit = actions.add(action)
	
	/**
	 * Print the node's name
	 * @return Node's name
	 */
	override def toString():String = name
}