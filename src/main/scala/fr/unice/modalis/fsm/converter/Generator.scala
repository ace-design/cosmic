package fr.unice.modalis.fsm.converter

import fr.unice.modalis.fsm.actions.unit.{Result, Action}
import fr.unice.modalis.fsm.core.Transition
import fr.unice.modalis.fsm.actions.constraints.Constraint

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
  def translate(a:Action,v:Set[Result]):(String,Set[Result])

  /**
   * Translate constraints
   * @param cl Constrains list
   * @return Constraints translated
   */
  def buildConstraints(cl:List[Constraint]):String
}

trait TransitionTranslator {
  /**
   * Translate a transition
   * @param t Transition
   * @return Translated transition
   */
  def translate(t:Transition):String

}
