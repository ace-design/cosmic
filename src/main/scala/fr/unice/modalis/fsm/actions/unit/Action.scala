package fr.unice.modalis.fsm.actions.unit

import fr.unice.modalis.fsm.guard.GuardAction


/**
 * Action mother trait
 */
trait Action {

  val guards: List[GuardAction]

  /**
   * Add guard on this action
   * @param g Guard
   * @return A copy of the current action object with the guard attached
   */
  def addGuard(g: GuardAction): Action

  override def toString(): String

}

/**
 * Result mother trait
 */
trait Result {
  val name: String
}

/**
 * Read result trait
 */
trait ReadResult extends Result {}

