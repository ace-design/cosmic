package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint
import scala.util.Random

/**
 * Created by cyrilcecchinel on 22/04/2014.
 */
case class SerialInit (val comPort:String, val result:SerialReference, val constraints:List[Constraint]) extends Action{

  def this(comPort:String, result:SerialReference) = this(comPort, result, List[Constraint]())

  override def toString():String = "INIT SERIAL:" + comPort + "(" + result.name + ")"
  override def addConstrain(co:Constraint):SerialInit = new SerialInit(comPort, result, co :: constraints)


}

case class SerialReference(val name:String) extends Result{
  def this() = this("var_"+ Random.alphanumeric.take(5).mkString)
}