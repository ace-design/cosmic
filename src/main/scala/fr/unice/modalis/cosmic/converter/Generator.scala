package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.actions.unit.{Result, Action}
import fr.unice.modalis.cosmic.core.Transition
import fr.unice.modalis.cosmic.actions.guard.GuardAction

/**
 * Action generator class
 */
trait ActionTranslator {
  /**
   * Translate an action
   * @param a action
   * @param v Current variable set
   * @return Translated action and new variables set
   */
  def translate(a: Action, v: Set[Result]): (String, Set[Result])

  /**
   * Translate constraint
   * @param cl Constrains list
   * @return Constraints translated
   */
  def buildConstraints(cl: List[GuardAction]): String
}

trait TransitionTranslator {
  /**
   * Translate a transition
   * @param t Transition
   * @return Translated transition
   */
  def translate(t: Transition): String

}