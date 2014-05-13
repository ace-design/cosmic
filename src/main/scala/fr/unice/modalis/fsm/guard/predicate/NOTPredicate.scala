package fr.unice.modalis.fsm.guard.predicate

import fr.unice.modalis.fsm.guard.GuardAction

/**
 * Created by cyrilcecchinel on 28/04/2014.
 */
case class NOTPredicate(val expression: GuardAction) extends Predicate {
  override def toString(): String = "NOT(" + expression + ")"

}
