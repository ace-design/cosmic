package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint
import scala.util.Random

/**
 * Emit actin
 * @param data Data to send
 * @param endpointURL Server url
 * @param endpointPort Server port
 * @param constraints Contraints list
 */
case class EmitAction(val data:ReadSerialResult, val endpointURL:String, val endpointPort:Int, val constraints:List[Constraint]) extends Action{
  def this(data:ReadSerialResult, url:String, port:Int) = this(data, url, port, List[Constraint]())
  def this(url:String, port:Int) = this(null, url, port, List[Constraint]())

  override def addConstrain(co:Constraint):EmitAction = new EmitAction(data, endpointURL, endpointPort, co :: constraints)

  override def toString():String = "EMIT ("+ endpointURL + ":" + endpointPort + ")"


}

/**
 * Emit result
 * @param name Name
 */
class EmitResult(val name:String) extends Result{
  def this() = this("var_"+ Random.alphanumeric.take(5).mkString)

}