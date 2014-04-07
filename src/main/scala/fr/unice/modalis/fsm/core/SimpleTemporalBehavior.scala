package fr.unice.modalis.fsm.core

import fr.unice.modalis.fsm.condition._

/**
 * Simple temporal behavior
 * Represent the simplest behavior possible (one node, time condition loop on this node)
 * @param a a First node
 * @param period period Period
 */
class SimpleTemporalBehavior(a:Node, period:Int)
  extends Behavior(a, Set[Node](a), Set[Transition](new Transition(a,a, new TickCondition(period)))){

  // Automata period
  val tickPeriod:Int = period

  /**
   * Get the action node
   * @return Action node
   */
  def actionNode:Node = a

}
