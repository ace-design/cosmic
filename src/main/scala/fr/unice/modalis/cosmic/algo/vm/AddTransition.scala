package fr.unice.modalis.cosmic.algo.vm

import fr.unice.modalis.cosmic.core.{Behavior, Transition}

/**
 * AddTransition class
 * Represent the action of adding a transition
 */
case class AddTransition(t: Transition) extends Instruction {
  override def make(b: Behavior): Behavior = b.addTransition(t)

  override def toString(): String = "{AddTransition transition=" + t + "}"
}