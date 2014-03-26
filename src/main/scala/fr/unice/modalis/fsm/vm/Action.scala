package fr.unice.modalis.fsm.vm

import fr.unice.modalis.fsm.core.Behavior

trait Action {
	def make(b: Behavior):Behavior
}

