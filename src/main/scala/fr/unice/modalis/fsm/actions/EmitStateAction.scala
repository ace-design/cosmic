package fr.unice.modalis.fsm.actions

import fr.unice.modalis.fsm.actions.constrains.Constrain

class EmitStateAction(url:String, port:Int, constrainsSet:Set[Constrain]) extends StateAction(constrainsSet:Set[Constrain]) {
  def this(url:String, port:Int) = this(url, port, Set[Constrain]())
	val endpointURL: String = url
	val endpointPort: Int = port

  override def addConstrain(co:Constrain):EmitStateAction = new EmitStateAction(url, port, constrains + co)

  override def toString():String = "EMIT " + endpointURL + ":" + endpointPort + " " + super.toString()
}