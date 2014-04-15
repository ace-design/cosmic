package fr.unice.modalis.fsm.actions

import fr.unice.modalis.fsm.actions.constraints.Constraint


case class OnAction(constraintL:List[Constraint]) extends Action(constraintL:List[Constraint]){

  def this() = this(List[Constraint]())

  override def toString():String = "ON"
  override def addConstrain(co:Constraint):OnAction = new OnAction(co :: constraints)


}
