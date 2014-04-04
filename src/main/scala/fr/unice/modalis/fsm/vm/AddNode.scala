package fr.unice.modalis.fsm.vm

import fr.unice.modalis.fsm.core.Behavior
import fr.unice.modalis.fsm.core.Node

/**
 * AddNode class
 * Represent the action of adding a node
 */
case class AddNode(n:Node) extends Action {
  
	override def make(b: Behavior):Behavior = b.addNode(n)

  override def toString():String = "{Addnode node=" + n + "}"

  
}