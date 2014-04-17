package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint

case class EmitAction( val endpointURL:String, val endpointPort:Int, val constraints:List[Constraint]) extends Action{
  def this(url:String, port:Int) = this(url, port, List[Constraint]())


  override def addConstrain(co:Constraint):EmitAction = new EmitAction(endpointURL, endpointPort, co :: constraints)

  override def toString():String = "EMIT ("+ endpointURL + ":" + endpointPort + ")"


}

class EmitResult() extends Result{

}