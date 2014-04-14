package fr.unice.modalis.fsm.actions

import fr.unice.modalis.fsm.actions.constraints.Constraint

case class EmitStateAction(url:String, port:Int, constraintL:List[Constraint]) extends StateAction(constraintL:List[Constraint]) {
  def this(url:String, port:Int) = this(url, port, List[Constraint]())
	val endpointURL: String = url
	val endpointPort: Int = port

  override def addConstrain(co:Constraint):EmitStateAction = new EmitStateAction(url, port, co :: constraints)

  override def toString():String = "EMIT " + endpointURL + ":" + endpointPort


}