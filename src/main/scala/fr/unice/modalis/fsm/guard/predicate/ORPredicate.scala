package fr.unice.modalis.fsm.guard.predicate

import fr.unice.modalis.fsm.guard.Guard

/**
 * Created by cyrilcecchinel on 28/04/2014.
 */
case class ORPredicate(val left: Guard, val right: Guard) extends Predicate {
  override def toString(): String = left + " OR " + right
}
