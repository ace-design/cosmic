package fr.unice.modalis.fsm.guard.constraint

import fr.unice.modalis.fsm.guard.Guard


/**
 * Action constrain parent class
 */
trait Action extends Guard {
  override def toString(): String
}
