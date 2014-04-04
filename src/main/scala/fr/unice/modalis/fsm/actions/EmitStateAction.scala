package fr.unice.modalis.fsm.actions

class EmitStateAction(url:String, port:Int) extends StateAction {
	val endpointURL: String = url
	val endpointPort: Int = port

  override def toString():String = "EMIT"
}