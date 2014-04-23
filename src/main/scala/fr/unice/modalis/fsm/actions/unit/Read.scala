package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint
import scala.util.Random

/**
 * Read data from a sensor
 * @param sensorId Sensor ID
 * @param result Read result
 * @param constraints Constraints list
 */
case class ReadSensorAction(val sensorId:String, val result:ReadSensorResult, val constraints:List[Constraint]) extends Action{

  def this(sensorId:String, result:ReadSensorResult) = this(sensorId, result, List[Constraint]())

  override def toString():String = "READ " + sensorId + " (" + result.name + ")"
  override def addConstrain(co:Constraint):ReadSensorAction = new ReadSensorAction(sensorId, result, co :: constraints)


}

/**
 * Read sensor Result
 * @param name Name
 */
case class ReadSensorResult(val name:String) extends ReadResult{
  def this() = this("var_"+ Random.alphanumeric.take(5).mkString)
}
