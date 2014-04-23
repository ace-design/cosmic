package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint


case class SendAction(val result:ReadSensorResult, val to:String, val constraints:List[Constraint]) extends Action{

  def this(result:ReadSensorResult, to:String) = this(result, to, List[Constraint]())

  // No communication port name : ie. Arduino boards
  def this(result:ReadSensorResult) = this(result, "", List[Constraint]())

  override def toString():String = "SEND" + (if (to != "") " " + to else "")
  override def addConstrain(co:Constraint):SendAction = new SendAction(result, to, co :: constraints)



}
case class SendResult(val name:String) extends Result{

}