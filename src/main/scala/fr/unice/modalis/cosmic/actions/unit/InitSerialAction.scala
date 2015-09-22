package fr.unice.modalis.cosmic.actions.unit

import fr.unice.modalis.cosmic.actions.guard.GuardAction

import scala.util.Random

/**
 * Initiate a serial port
 * @param comPort Serial port
 * @param result Serial Init result (reference to Serial Port)
 * @param guards Constraints list
 */
case class InitSerialAction(val comPort: String, val result: InitSerialVariable, val guards: List[GuardAction]) extends Action {

  def this(comPort: String, result: InitSerialVariable) = this(comPort, result, List[GuardAction]())

  override def toString(): String = "INIT SERIAL:" + comPort + "(" + result.name + ")"

  override def addGuard(co: GuardAction): InitSerialAction = new InitSerialAction(comPort, result, co :: guards)


}

/**
 * Serial Init Result
 * @param name Name
 */
case class InitSerialVariable(val name: String) extends Variable {
  def this() = this("var_" + Random.alphanumeric.take(5).mkString)
}