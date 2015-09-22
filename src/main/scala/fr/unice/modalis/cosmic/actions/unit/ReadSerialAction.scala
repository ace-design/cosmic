package fr.unice.modalis.cosmic.actions.unit

import fr.unice.modalis.cosmic.actions.guard.GuardAction

import scala.util.Random

/**
 * Read from serial port
 * @param comPort Serial port reference
 * @param result Read Serial result
 * @param guards Constraint list
 */
case class ReadSerialAction(val comPort: InitSerialVariable, val result: ReadVariable, val guards: List[GuardAction]) extends Action {

  def this(comPort: InitSerialVariable, result: ReadVariable) = this(comPort, result, List[GuardAction]())

  // No initialization mandatory (ie. Arduino boards)
  def this(result: ReadVariable) = this(new InitSerialVariable(), result, List[GuardAction]())

  override def toString(): String = "READ SERIAL ref:" + comPort.name + " (" + result.name + ")"

  override def addGuard(co: GuardAction): ReadSerialAction = new ReadSerialAction(comPort, result, co :: guards)


}

/**
 * Read serial result
 * @param name Name
 */
case class ReadSerialVariable(val name: String) extends ReadVariable {
  def this() = this("var_" + Random.alphanumeric.take(5).mkString)
}
