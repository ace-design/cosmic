package fr.unice.modalis.cosmic.algo.vm

import fr.unice.modalis.cosmic.core.{Behavior, Node}

/**
 * DeleteNode class
 * Represent the action of deleting a node
 */
case class DeleteNode(n: Node) extends Instruction {
  override def make(b: Behavior): Behavior = b.deleteNode(n)

  override def toString(): String = "{Delete node=" + n + "}"
}