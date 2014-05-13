package fr.unice.modalis.fsm.actions.unit

import scala.util.Random
import fr.unice.modalis.fsm.guard.Guard

/**
 * Read data from a sensor
 * @param sensorId Sensor ID
 * @param result Read result
 * @param guards Constraints list
 */
case class ReadSensorAction(val sensorId: String, val result: ReadSensorResult, val guards: List[Guard]) extends Action {

  def this(sensorId: String, result: ReadSensorResult) = this(sensorId, result, List[Guard]())

  override def toString(): String = "READ " + sensorId + " (" + result.name + ")"

  override def addGuard(co: Guard): ReadSensorAction = new ReadSensorAction(sensorId, result, co :: guards)


}

/**
 * Read sensor Result
 * @param name Name
 */
case class ReadSensorResult(val name: String) extends ReadResult {
  def this() = this("var_" + Random.alphanumeric.take(5).mkString)
}
