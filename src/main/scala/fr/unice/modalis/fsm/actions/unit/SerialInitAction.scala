package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint
import scala.util.Random

/**
 * Initiate a serial port
 * @param comPort Serial port
 * @param result Serial Init result (reference to Serial Port)
 * @param constraints Constraints list
 */
case class SerialInitAction (val comPort:String, val result:SerialInitResult, val constraints:List[Constraint]) extends Action{

  def this(comPort:String, result:SerialInitResult) = this(comPort, result, List[Constraint]())

  override def toString():String = "INIT SERIAL:" + comPort + "(" + result.name + ")"
  override def addConstrain(co:Constraint):SerialInitAction = new SerialInitAction(comPort, result, co :: constraints)


}

/**
 * Serial Init Result
 * @param name Name
 */
case class SerialInitResult(val name:String) extends Result{
  def this() = this("var_"+ Random.alphanumeric.take(5).mkString)
}