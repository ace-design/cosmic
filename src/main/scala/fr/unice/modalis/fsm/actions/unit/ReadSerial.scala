package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint
import scala.util.Random

/**
 * Created by cyrilcecchinel on 22/04/2014.
 */
case class ReadSerial(val comPort:SerialReference, val result:ReadSerialResult, val constraints:List[Constraint]) extends Action{

  def this(comPort:SerialReference, result:ReadSerialResult) = this(comPort, result, List[Constraint]())

  override def toString():String = "READ SERIAL ref:" + comPort.name
  override def addConstrain(co:Constraint):ReadSerial = new ReadSerial(comPort, result, co :: constraints)


}

case class ReadSerialResult(val name:String) extends Result{
  def this() = this("var_"+ Random.alphanumeric.take(5).mkString)
}
