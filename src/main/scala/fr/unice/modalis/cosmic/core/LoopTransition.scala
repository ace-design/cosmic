package fr.unice.modalis.cosmic.core

import fr.unice.modalis.cosmic.core.condition.Condition

/**
 * Loop transition
 * @param n Node
 * @param c Condition
 */
class LoopTransition(val n:Node, val c:Condition) extends Transition(n,n,c) {

}
