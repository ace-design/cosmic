package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint

case class EmitAction(url:String, port:Int, constraintL:List[Constraint]) extends Action(constraintL:List[Constraint]) {
  def this(url:String, port:Int) = this(url, port, List[Constraint]())

  val endpointURL: String = url
	val endpointPort: Int = port

  override def addConstrain(co:Constraint):EmitAction = new EmitAction(url, port, co :: constraints)

  override def toString():String = "EMIT " + endpointURL + ":" + endpointPort


}