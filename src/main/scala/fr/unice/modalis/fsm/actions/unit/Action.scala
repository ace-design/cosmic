package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.actions.constraints.Constraint

/**
 * Action mother trait
 */
trait Action {

  val constraints:List[Constraint]

  /**
   * Add constraint on this action
   * @param co Constraint
   * @return A copy of the current action object with the contraint attached
   */
  def addConstrain(co:Constraint):Action

  override def toString(): String

}

/**
 * Result mother trait
 */
trait Result { val name:String }

/**
 * Read result trait
 */
trait ReadResult extends Result {}