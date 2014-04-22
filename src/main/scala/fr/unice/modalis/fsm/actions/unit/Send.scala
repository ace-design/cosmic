package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint


case class SendAction(val data:ReadResult, val to:String, val constraints:List[Constraint]) extends Action{

  def this(data:ReadResult, toSend:String) = this(data, toSend, List[Constraint]())

  override def toString():String = "SEND to: " + to
  override def addConstrain(co:Constraint):SendAction = new SendAction(data, to, co :: constraints)



}
case class SendResult(val name:String) extends Result{

}