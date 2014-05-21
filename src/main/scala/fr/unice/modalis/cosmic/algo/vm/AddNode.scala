package fr.unice.modalis.cosmic.algo.vm

import fr.unice.modalis.cosmic.core.Behavior
import fr.unice.modalis.cosmic.core.Node

/**
 * AddNode class
 * Represent the action of adding a node
 */
case class AddNode(n: Node) extends Instruction {

  override def make(b: Behavior): Behavior = b.addNode(n)

  override def toString(): String = "{Addnode node=" + n + "}"


}