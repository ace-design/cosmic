package fr.unice.modalis.fsm.guard.predicate

import fr.unice.modalis.fsm.guard.GuardAction

/**
 * Created by cyrilcecchinel on 28/04/2014.
 */
case class ORPredicate(val left: GuardAction, val right: GuardAction) extends Predicate {
  override def toString(): String = left + " OR " + right
}
