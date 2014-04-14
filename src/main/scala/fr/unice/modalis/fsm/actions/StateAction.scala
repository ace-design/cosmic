package fr.unice.modalis.fsm.actions

import fr.unice.modalis.fsm.actions.constraints.Constraint

abstract class StateAction(c:List[Constraint]) {

  def this() = this(List[Constraint]())
  val constraints:List[Constraint] = c

  def addConstrain(co:Constraint):StateAction

  override def toString(): String

}