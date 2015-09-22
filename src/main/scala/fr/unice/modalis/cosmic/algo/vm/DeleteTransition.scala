package fr.unice.modalis.cosmic.algo.vm

import fr.unice.modalis.cosmic.core.{Behavior, Transition}

/**
 * DeleteTransistion class
 * Represent the action of deleting a transition
 */
case class DeleteTransition(t: Transition) extends Instruction {
  override def make(b: Behavior): Behavior = b.deleteTransition(t)

  override def toString(): String = "{Delete transition=" + t + "}"
}