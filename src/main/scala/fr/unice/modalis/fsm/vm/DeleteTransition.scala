package fr.unice.modalis.fsm.vm

import fr.unice.modalis.fsm.core.Transition
import fr.unice.modalis.fsm.core.Behavior

/**
 * DeleteTransistion class
 * Represent the action of deleting a transition
 */
case class DeleteTransition(t: Transition) extends Action{
  override def make(b: Behavior):Behavior = b.deleteTransition(t)

  override def toString():String = "{Delete transition=" + t + "}"
}