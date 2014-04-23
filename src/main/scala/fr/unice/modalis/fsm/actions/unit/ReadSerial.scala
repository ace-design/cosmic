package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint
import scala.util.Random

/**
 * Read from serial port
 * @param comPort Serial port reference
 * @param result Read Serial result
 * @param constraints Constraint list
 */
case class ReadSerial(val comPort:SerialInitResult, val result:ReadSerialResult, val constraints:List[Constraint]) extends Action{

  def this(comPort:SerialInitResult, result:ReadSerialResult) = this(comPort, result, List[Constraint]())

  // No initialization mandatory (ie. Arduino boards)
  def this(result:ReadSerialResult) = this(new SerialInitResult(), result, List[Constraint]())

  override def toString():String = "READ SERIAL ref:" + comPort.name + " (" + result.name + ")"
  override def addConstrain(co:Constraint):ReadSerial = new ReadSerial(comPort, result, co :: constraints)


}

/**
 * Read serial result
 * @param name Name
 */
case class ReadSerialResult(val name:String) extends ReadResult{
  def this() = this("var_"+ Random.alphanumeric.take(5).mkString)
}
