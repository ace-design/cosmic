package fr.unice.modalis.fsm.vm

import fr.unice.modalis.fsm.core.Behavior
import fr.unice.modalis.fsm.core.Transition

/**
 * AddTransition class
 * Represent the action of adding a transition
 */
case class AddTransition(t:Transition) extends Action {
	override def make(b: Behavior): Behavior = b.addTransition(t)
}