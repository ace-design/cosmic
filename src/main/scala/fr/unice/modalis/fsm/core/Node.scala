package fr.unice.modalis.fsm.core

import fr.unice.modalis.fsm.actions.StateAction
import scala.collection.mutable.Set
/**
 * Node class
 * This class represents a FSM node with actions attached 
 */
case class Node(nodeName: String, actionsSet:Set[StateAction]) {

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
   * Compose two nodes
   * @param n Other node
   * @return A composed node
   */
  def +(n:Node):Node =
  {
    val actionsComposed = this.actions.union(n.actions)
    val processedNode:Node = new Node(this.name + "_" + n.name)
    actionsComposed.foreach(a => processedNode.addAction(a))
    processedNode
  }

  /**
   * Count how many constrains are fixed on this node's actions
   * @return Amount of constrains
   */
  def constraintsAmount():Int =
  {
    var i = 0
    actions.foreach(a => i = i + a.constraints.length)
    i
  }
	/**
	 * Print the node's name
	 * @return Node's name
	 */
	override def toString():String = name

}