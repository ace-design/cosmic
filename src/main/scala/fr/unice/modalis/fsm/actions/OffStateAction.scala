package fr.unice.modalis.fsm.actions

import fr.unice.modalis.fsm.actions.constraints.Constraint

case class OffStateAction(constraintL:List[Constraint]) extends StateAction(constraintL:List[Constraint]){

  def this() = this(List[Constraint]())
  override def addConstrain(co:Constraint):OffStateAction = new OffStateAction(co :: constraints)
  override def toString():String = "OFF"


}