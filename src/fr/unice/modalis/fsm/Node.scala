package fr.unice.modalis.fsm

import scala.collection.mutable.Set
import fr.unice.modalis.fsm.actions.Action

/**
 * Node class
 */
class Node {
	val actions = Set[Action]()
	
	/**
	 * Add an action for the current Node
	 * @param action Action to add
	 * @return True if the action has been added, false otherwise
	 */
	def addAction(action:Action):Boolean = actions.add(action)
}