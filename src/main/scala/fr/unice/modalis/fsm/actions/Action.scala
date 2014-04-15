package fr.unice.modalis.fsm.actions

import fr.unice.modalis.fsm.actions.constraints.Constraint

abstract class Action(c:List[Constraint]) {

  def this() = this(List[Constraint]())
  val constraints:List[Constraint] = c

  def addConstrain(co:Constraint):Action

  override def toString(): String

}