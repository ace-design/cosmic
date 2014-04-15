package fr.unice.modalis.fsm.vm

import fr.unice.modalis.fsm.core.Behavior

trait VMAction {
	def make(b: Behavior):Behavior
  override def toString():String
}

