package fr.unice.modalis.fsm.core

import fr.unice.modalis.fsm.condition._

/**
 * Simple temporal behavior
 * Represent the simplest behavior possible (two node, time condition between those nodes)
 * @param a a First node
 * @param b b Second node
 * @param period period Period
 */
class SimpleTemporalBehavior(a:Node, b:Node, period:Int)
  extends Behavior(a, Set[Node](a,b), Set[Transition](new Transition(a,b, new TickCondition(period)), new Transition(b,a, new TrueCondition))){

  // Automata period
  val tickPeriod:Int = period

  /**
   * Get the action node
   * @return Action node
   */
  def actionNode:Node = b

}
