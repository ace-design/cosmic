package fr.unice.modalis.fsm.vm

import fr.unice.modalis.fsm.core.Behavior

trait Instruction {
	def make(b: Behavior):Behavior
  override def toString():String
}

