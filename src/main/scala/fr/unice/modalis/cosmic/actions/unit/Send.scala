package fr.unice.modalis.cosmic.actions.unit

import fr.unice.modalis.cosmic.actions.guard.GuardAction


case class WriteSerialAction(val result: ReadSensorResult, val to: String, val guards: List[GuardAction]) extends Action {

  def this(result: ReadSensorResult, to: String) = this(result, to, List[GuardAction]())

  // No communication port name : ie. Arduino boards
  def this(result: ReadSensorResult) = this(result, "", List[GuardAction]())

  override def toString(): String = "SEND" + (if (to != "") " " + to else "")

  override def addGuard(co: GuardAction): WriteSerialAction = new WriteSerialAction(result, to, co :: guards)


}

case class WriteSerialResult(val name: String) extends Result {

}