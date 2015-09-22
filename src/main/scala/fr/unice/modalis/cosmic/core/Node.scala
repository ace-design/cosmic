package fr.unice.modalis.cosmic.core

import fr.unice.modalis.cosmic.actions.flow.SequentialActions
import fr.unice.modalis.cosmic.actions.unit.Action

import scala.util.Random

/**
 * Node class
 * This class represents a FSM node with actions attached 
 */
case class Node(val name: String, val actions: SequentialActions) {

  def this(nodeName: String) = this(nodeName, new SequentialActions)

  def this() = this("node_" + Random.alphanumeric.take(5).mkString)
  /**
   * Add an action for the current Node
   * @param action Action to add
   * @return Copied node with new action added
   */
  def addAction(action: Action): Node = new Node(name, actions.add(action))

  /**
   * Compose two nodes
   * @param n Other node
   * @return A composed node
   */
  def +(n: Node): Node = {
    new Node(this.name + "_" + n.name, actions.union(n.actions))
  }

  /**
   * Count how many constrains are fixed on this node's actions
   * @return Amount of constrains
   */
  def constraintsAmount(): Int = {
    var i = 0
    actions.actions.foreach(a => i = i + a.guards.length)
    i
  }

  /**
   * Print the node's name
   * @return Node's name
   */
  override def toString(): String = name

}