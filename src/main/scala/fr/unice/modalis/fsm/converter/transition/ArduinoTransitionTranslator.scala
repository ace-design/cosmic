package fr.unice.modalis.fsm.converter.transition

import fr.unice.modalis.fsm.core.Transition
import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.converter.TransitionTranslator

/**
 * Arduino transition translator
 */
object ArduinoTransitionTranslator extends TransitionTranslator {
  /**
   * Translate a transition for Arduino
   * @param t Transition
   * @return Translated transition
   */
  def translate(t: Transition): String = t.condition match {
    case TickCondition(n) => "delay(" + n * 1000 + ");\n"
    case _ => throw new Exception("Transition " + t + " not handled on Arduino plateform")
  }

}
