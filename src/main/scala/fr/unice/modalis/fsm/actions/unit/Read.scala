package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint

case class ReadAction(val sensorId:String, val constraints:List[Constraint]) extends Action{

  def this(sensorId:String) = this(sensorId, List[Constraint]())

  override def toString():String = "READ " + sensorId
  override def addConstrain(co:Constraint):ReadAction = new ReadAction(sensorId, co :: constraints)


}

case class ReadResult() extends Result{

}
