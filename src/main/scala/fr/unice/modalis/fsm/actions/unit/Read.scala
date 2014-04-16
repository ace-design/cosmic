package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint

class ReadAction(val sensorId:String, constraintL:List[Constraint]) extends Action(constraintL:List[Constraint]){

  def this(sensorId:String) = this(sensorId, List[Constraint]())

  override def toString():String = "READ " + sensorId
  override def addConstrain(co:Constraint):ReadAction = new ReadAction(sensorId, co :: constraints)


}

class ReadResult() extends Result{

}
