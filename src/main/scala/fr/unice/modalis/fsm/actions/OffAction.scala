package fr.unice.modalis.fsm.actions

import fr.unice.modalis.fsm.actions.constraints.Constraint

case class OffAction(constraintL:List[Constraint]) extends Action(constraintL:List[Constraint]){

  def this() = this(List[Constraint]())
  override def addConstrain(co:Constraint):OffAction = new OffAction(co :: constraints)
  override def toString():String = "OFF"


}