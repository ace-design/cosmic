package fr.unice.modalis.fsm.guard.predicate

import fr.unice.modalis.fsm.guard.Guard

/**
 * Created by cyrilcecchinel on 28/04/2014.
 */
case class NOTPredicate(val expression: Guard) extends Predicate {
  override def toString(): String = "NOT(" + expression + ")"

}
