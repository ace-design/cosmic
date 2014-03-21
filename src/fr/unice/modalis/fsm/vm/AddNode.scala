package fr.unice.modalis.fsm.vm


import fr.unice.modalis.fsm.core.Behavior
import fr.unice.modalis.fsm.core.Node

case class AddNode(n:Node) extends Action {
  
	override def make(b: Behavior):Behavior = b.addNode(n)
	

  
}