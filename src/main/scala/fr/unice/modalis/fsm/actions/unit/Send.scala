package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.guard.Guard


case class SendAction(val result: ReadSensorResult, val to: String, val guards: List[Guard]) extends Action {

  def this(result: ReadSensorResult, to: String) = this(result, to, List[Guard]())

  // No communication port name : ie. Arduino boards
  def this(result: ReadSensorResult) = this(result, "", List[Guard]())

  override def toString(): String = "SEND" + (if (to != "") " " + to else "")

  override def addGuard(co: Guard): SendAction = new SendAction(result, to, co :: guards)


}

case class SendResult(val name: String) extends Result {

}