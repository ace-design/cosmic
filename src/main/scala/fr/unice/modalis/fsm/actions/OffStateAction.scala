package fr.unice.modalis.fsm.actions

import fr.unice.modalis.fsm.actions.constrains.Constrain

class OffStateAction(constrainsSet:Set[Constrain]) extends StateAction(constrainsSet:Set[Constrain]){

  def this() = this(Set[Constrain]())
  override def addConstrain(co:Constrain):OffStateAction = new OffStateAction(constrains + co)
  override def toString():String = "OFF" + " " + super.toString()
}