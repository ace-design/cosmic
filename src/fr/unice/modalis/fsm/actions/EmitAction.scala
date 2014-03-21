package fr.unice.modalis.fsm.actions

class EmitAction(url:String, port:Int) extends Action {
	val endpointURL: String = url
	val endpointPort: Int = port
	
	override def toString():String = "Emit to " + endpointURL + ":" + endpointPort
}