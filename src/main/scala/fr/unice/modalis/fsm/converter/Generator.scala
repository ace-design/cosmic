package fr.unice.modalis.fsm.converter

import fr.unice.modalis.fsm.actions.unit.{Result, Action}
import fr.unice.modalis.fsm.core.Transition

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

}

trait TransitionTranslator {
  /**
   * Translate a transition
   * @param t Transition
   * @return Translated transition
   */
  def translate(t:Transition):String

}
