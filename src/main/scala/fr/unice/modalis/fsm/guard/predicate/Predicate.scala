package fr.unice.modalis.fsm.guard.predicate

import fr.unice.modalis.fsm.guard.GuardAction

/**
 * Created by cyrilcecchinel on 13/05/2014.
 */
trait Predicate extends GuardAction {
  override def toString(): String

}
