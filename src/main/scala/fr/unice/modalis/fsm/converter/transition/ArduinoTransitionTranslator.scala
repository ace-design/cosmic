package fr.unice.modalis.fsm.converter.transition

import fr.unice.modalis.fsm.core.Transition
import fr.unice.modalis.fsm.condition.{TrueCondition, TickCondition}
import fr.unice.modalis.fsm.converter.TransitionTranslator

/**
 * Created by cyrilcecchinel on 22/04/2014.
 */
object ArduinoTransitionTranslator extends TransitionTranslator {
  /**
   * Translate a transition for Arduino
   * @param t Transition
   * @return Translated transition
   */
  def translate(t:Transition):String = t.condition match {
    case TickCondition(n) => "delay(" + n*1000 + ");\n"
    case TrueCondition() => "\n"
    case _ => throw new Exception("Transition " + t + " not handled on Arduino plateform")
  }

}
