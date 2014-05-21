package fr.unice.modalis.cosmic.actions.unit

import scala.util.Random
import fr.unice.modalis.cosmic.actions.guard.GuardAction

/**
 * Initiate a serial port
 * @param comPort Serial port
 * @param result Serial Init result (reference to Serial Port)
 * @param guards Constraints list
 */
case class InitSerialAction(val comPort: String, val result: InitSerialResult, val guards: List[GuardAction]) extends Action {

  def this(comPort: String, result: InitSerialResult) = this(comPort, result, List[GuardAction]())

  override def toString(): String = "INIT SERIAL:" + comPort + "(" + result.name + ")"

  override def addGuard(co: GuardAction): InitSerialAction = new InitSerialAction(comPort, result, co :: guards)


}

/**
 * Serial Init Result
 * @param name Name
 */
case class InitSerialResult(val name: String) extends Result {
  def this() = this("var_" + Random.alphanumeric.take(5).mkString)
}