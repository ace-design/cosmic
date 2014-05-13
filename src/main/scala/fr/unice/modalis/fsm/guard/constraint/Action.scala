package fr.unice.modalis.fsm.guard.constraint

import fr.unice.modalis.fsm.guard.GuardAction


/**
 * Action constrain parent class
 */
trait Action extends GuardAction {
  override def toString(): String
}
