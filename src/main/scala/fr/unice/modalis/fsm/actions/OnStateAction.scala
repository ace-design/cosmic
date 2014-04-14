package fr.unice.modalis.fsm.actions

import fr.unice.modalis.fsm.actions.constraints.Constraint


case class OnStateAction(constraintL:List[Constraint]) extends StateAction(constraintL:List[Constraint]){

  def this() = this(List[Constraint]())

  override def toString():String = "ON"
  override def addConstrain(co:Constraint):OnStateAction = new OnStateAction(co :: constraints)


}
