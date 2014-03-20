package fr.unice.modalis.fsm.condition

import fr.unice.modalis.fsm.condition.ConditionType._


/**
 * Tick condition
 * @constructor frequency
 */
class TickCondition(freq:Int) extends Condition {
	val frequency:Int = freq
}