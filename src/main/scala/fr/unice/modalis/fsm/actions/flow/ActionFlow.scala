package fr.unice.modalis.fsm.actions.flow

import fr.unice.modalis.fsm.actions.unit.Action

/**
 * Action flow
 */
abstract class ActionFlow {

  val actions: Iterable[Action]

  def add(a: Action): ActionFlow


  def size: Int


  def union(s: SequentialActions): ActionFlow

}
