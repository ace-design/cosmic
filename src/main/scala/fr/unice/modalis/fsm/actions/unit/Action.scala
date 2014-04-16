package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint

/**
 * Action mother class
 * @param constraints Constraint list
 */
abstract class Action(val constraints:List[Constraint]) {

  /**
   * Create Action with an empty constraint list
   * @return
   */
  def this() = this(List[Constraint]())

  /**
   * Add constraint on this action
   * @param co Constraint
   * @return A copy of the current action object with the contraint attached
   */
  def addConstrain(co:Constraint):Action

  override def toString(): String

}

trait Result {}