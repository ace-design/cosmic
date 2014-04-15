package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint

/**
 * Created by cyrilcecchinel on 15/04/2014.
 */
case class SendAction(val to:String, constraintL:List[Constraint]) extends Action(constraintL:List[Constraint]){

  def this(toSend:String) = this(toSend, List[Constraint]())

  override def toString():String = "SEND to: " + to
  override def addConstrain(co:Constraint):SendAction = new SendAction(to, co :: constraints)


}
