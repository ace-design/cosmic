package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint
import scala.util.Random

case class ReadAction(val sensorId:String, val result:ReadResult, val constraints:List[Constraint]) extends Action{

  def this(sensorId:String, result:ReadResult) = this(sensorId, result, List[Constraint]())

  override def toString():String = "READ " + sensorId
  override def addConstrain(co:Constraint):ReadAction = new ReadAction(sensorId, result, co :: constraints)


}

case class ReadResult(val name:String) extends Result{
  def this() = this("var_"+ Random.alphanumeric.take(5).mkString)
}
