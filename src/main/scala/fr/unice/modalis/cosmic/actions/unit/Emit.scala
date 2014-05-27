package fr.unice.modalis.cosmic.actions.unit

import scala.util.Random
import fr.unice.modalis.cosmic.actions.guard.GuardAction

/**
 * Emit actin
 * @param data Data to send
 * @param endpointURL Server url
 * @param endpointPort Server port
 * @param guards Contraints list
 */
case class EmitAction(val data: ReadResult, val endpointURL: String, val endpointPort: Int, val guards: List[GuardAction]) extends Action {
  def this(data: ReadResult, url: String, port: Int) = this(data, url, port, List[GuardAction]())

  def this(url: String, port: Int) = this(null, url, port, List[GuardAction]())

  override def addGuard(co: GuardAction): EmitAction = new EmitAction(data, endpointURL, endpointPort, co :: guards)

  override def toString(): String = "EMIT " + (if (data != null) data.name else "") + " (" + endpointURL + ":" + endpointPort + ")"


}

/**
 * Emit result
 * @param name Name
 */
class EmitResult(val name: String) extends Result {
  def this() = this("var_" + Random.alphanumeric.take(5).mkString)

}