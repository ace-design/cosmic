package fr.unice.modalis.cosmic.converter.transition

import fr.unice.modalis.cosmic.converter.TransitionTranslator
import fr.unice.modalis.cosmic.core.Transition
import fr.unice.modalis.cosmic.core.condition.TickCondition

/**
 * Raspberry Pi transition translator (in python)
 */
object RaspberryTransitionTranslator extends TransitionTranslator {
  /**
   * Translate a transition for Raspberry
   * @param t Transition
   * @return Translated transition
   */
  def translate(t: Transition): String = t.condition match {
    case TickCondition(n) => "\t\ttime.sleep(" + n + ");\n"
    case _ => throw new Exception("Transition " + t + " not handled on Raspberry plateform")
  }
}
