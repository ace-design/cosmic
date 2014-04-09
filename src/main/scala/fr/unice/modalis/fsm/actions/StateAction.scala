package fr.unice.modalis.fsm.actions

import fr.unice.modalis.fsm.actions.constrains.Constrain

abstract class StateAction(c:Set[Constrain]) {

  def this() = this(Set[Constrain]())
  val constrains:Set[Constrain] = c

  def addConstrain(co:Constrain):StateAction

  override def toString(): String = constrains.toString()
}