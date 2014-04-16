package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint


case class SendAction(val to:String, constraintL:List[Constraint]) extends Action(constraintL:List[Constraint]){

  def this(toSend:String) = this(toSend, List[Constraint]())

  override def toString():String = "SEND to: " + to
  override def addConstrain(co:Constraint):SendAction = new SendAction(to, co :: constraints)



}
case class SendResult(val p:ReadResult) extends Result{

}