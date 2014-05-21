package fr.unice.modalis.cosmic.actions.guard.constraint

import fr.unice.modalis.cosmic.actions.unit.ReadResult

/**
 * Value Constraint
 * Fix a constraint upon a value  Value op threshold
 * @constructor Create a new value constraint
 * @param value Value to compare
 * @param threshold Threshold
 * @param operator Operator
 */
case class ValueConstraint(val value: ReadResult, val threshold: Int, val operator: String) extends Constraint {

  def isCorrect(o: String): Boolean = {
    o == ">" || o == "<" || o == "==" || o == ">=" || o == "<=" || o == "!="
  }

  if (!isCorrect(operator)) throw new Exception("Bad operator for " + operator)

  override def toString(): String = value.name + "\\" + operator + threshold


}
