package fr.unice.modalis.cosmic.actions.guard.constraint

import fr.unice.modalis.cosmic.actions.guard.GuardAction


/**
 * Action constrain parent class
 */
trait Action extends GuardAction {
  override def toString(): String
}
