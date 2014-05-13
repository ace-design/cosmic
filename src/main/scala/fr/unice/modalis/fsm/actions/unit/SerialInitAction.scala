package fr.unice.modalis.fsm.actions.unit

import scala.util.Random
import fr.unice.modalis.fsm.guard.GuardAction

/**
 * Initiate a serial port
 * @param comPort Serial port
 * @param result Serial Init result (reference to Serial Port)
 * @param guards Constraints list
 */
case class SerialInitAction(val comPort: String, val result: SerialInitResult, val guards: List[GuardAction]) extends Action {

  def this(comPort: String, result: SerialInitResult) = this(comPort, result, List[GuardAction]())

  override def toString(): String = "INIT SERIAL:" + comPort + "(" + result.name + ")"

  override def addGuard(co: GuardAction): SerialInitAction = new SerialInitAction(comPort, result, co :: guards)


}

/**
 * Serial Init Result
 * @param name Name
 */
case class SerialInitResult(val name: String) extends Result {
  def this() = this("var_" + Random.alphanumeric.take(5).mkString)
}