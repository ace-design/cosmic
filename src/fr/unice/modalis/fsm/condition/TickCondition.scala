package fr.unice.modalis.fsm.condition

import fr.unice.modalis.fsm.condition.ConditionType._


/**
 * Tick condition
 * @constructor frequency
 */
case class TickCondition(freq:Int) extends Condition {
	val frequency:Int = freq
	
	override def toString():String = "TICK: " + freq
}