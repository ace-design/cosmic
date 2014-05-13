package fr.unice.modalis.fsm.actions.unit

import scala.util.Random
import fr.unice.modalis.fsm.guard.GuardAction

/**
 * Read from serial port
 * @param comPort Serial port reference
 * @param result Read Serial result
 * @param guards Constraint list
 */
case class ReadSerial(val comPort: SerialInitResult, val result: ReadSerialResult, val guards: List[GuardAction]) extends Action {

  def this(comPort: SerialInitResult, result: ReadSerialResult) = this(comPort, result, List[GuardAction]())

  // No initialization mandatory (ie. Arduino boards)
  def this(result: ReadSerialResult) = this(new SerialInitResult(), result, List[GuardAction]())

  override def toString(): String = "READ SERIAL ref:" + comPort.name + " (" + result.name + ")"

  override def addGuard(co: GuardAction): ReadSerial = new ReadSerial(comPort, result, co :: guards)


}

/**
 * Read serial result
 * @param name Name
 */
case class ReadSerialResult(val name: String) extends ReadResult {
  def this() = this("var_" + Random.alphanumeric.take(5).mkString)
}
