package fr.unice.modalis.cosmic.actions.unit

import fr.unice.modalis.cosmic.actions.guard.GuardAction

import scala.util.Random

/**
 * Read data from a sensor
 * @param sensorId Sensor ID
 * @param result Read result
 * @param guards Constraints list
 */
case class ReadSensorAction(val sensorId: String, val result: ReadSensorVariable, val guards: List[GuardAction]) extends Action {

  def this(sensorId: String, result: ReadSensorVariable) = this(sensorId, result, List[GuardAction]())

  override def toString(): String = "READ " + sensorId + " (" + result.name + ")"

  override def addGuard(co: GuardAction): ReadSensorAction = new ReadSensorAction(sensorId, result, co :: guards)


}

/**
 * Read sensor Result
 * @param name Name
 */
case class ReadSensorVariable(val name: String) extends ReadVariable {
  def this() = this("var_" + Random.alphanumeric.take(5).mkString)
}
