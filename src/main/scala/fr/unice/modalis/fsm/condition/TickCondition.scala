package fr.unice.modalis.fsm.condition


/**
 * Tick condition
 * @constructor frequency
 */
case class TickCondition(freq:Int) extends Condition {
	val frequency:Int = freq
	
	override def toString():String = "TICK: " + freq
}