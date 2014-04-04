package fr.unice.modalis.fsm.core

import fr.unice.modalis.fsm.condition.Condition

/**
 * Transition class
 * @constructor Build a transition between 2 nodes upon a condition
 */
class Transition(src: Node, dst: Node, cond: Condition) {
	val source: Node = src
	val destination: Node = dst
	val condition: Condition = cond
	
	override def toString():String = "{" + source + "=>" + destination + "," + condition + "}"


}