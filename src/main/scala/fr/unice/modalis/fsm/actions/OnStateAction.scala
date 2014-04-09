package fr.unice.modalis.fsm.actions

import fr.unice.modalis.fsm.actions.constrains.Constrain


class OnStateAction(constrainsSet:Set[Constrain]) extends StateAction(constrainsSet:Set[Constrain]){

  def this() = this(Set[Constrain]())

  override def toString():String = "ON " + " " + super.toString()
  override def addConstrain(co:Constrain):OnStateAction = new OnStateAction(constrains + co)
}
