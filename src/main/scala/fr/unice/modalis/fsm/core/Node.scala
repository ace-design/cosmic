package fr.unice.modalis.fsm.core

import fr.unice.modalis.fsm.actions.unit.Action
import fr.unice.modalis.fsm.actions.flow.SequentialActions

/**
 * Node class
 * This class represents a FSM node with actions attached 
 */
case class Node(val name: String, val actions:SequentialActions) {

  def this(nodeName:String) = this(nodeName, new SequentialActions)
	
	/**
	 * Add an action for the current Node
	 * @param action Action to add
   * @return Copied node with new action added
	 */
	def addAction(action:Action):Node = new Node(name, actions.add(action))

  /**
   * Compose two nodes
   * @param n Other node
   * @return A composed node
   */
  def +(n:Node):Node =
  {
    new Node(this.name + "_" + n.name, actions.union(n.actions))
  }

  /**
   * Count how many constrains are fixed on this node's actions
   * @return Amount of constrains
   */
  def constraintsAmount():Int =
  {
    var i = 0
    actions.actions.foreach(a => i = i + a.constraints.length)
    i
  }
	/**
	 * Print the node's name
	 * @return Node's name
	 */
	override def toString():String = name

}