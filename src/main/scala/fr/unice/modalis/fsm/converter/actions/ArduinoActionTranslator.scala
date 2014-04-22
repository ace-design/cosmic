package fr.unice.modalis.fsm.converter.actions

import fr.unice.modalis.fsm.actions.unit._
import fr.unice.modalis.fsm.converter.ActionTranslator
import fr.unice.modalis.fsm.actions.unit.ReadAction
import fr.unice.modalis.fsm.actions.unit.SendAction

/**
 * Translate actions for Arduino
 */
object ArduinoActionTranslator extends ActionTranslator{
  /**
   * Translate an action
   * @param a action Action
   * @param v Current variables set
   * @return Translated action and new variables set
   */

  def translate(a:Action,v:Set[Result]):(String,Set[Result]) = {
    a match {
      case SendAction(data, to, _) => ("Serial.println(" + data.name + ");\n", v)
      case ReadAction(id, result, _) => (result.name + " = analogRead(" + convertId(id) + ");\n",v + result)
      case _ => throw new Exception("Action " + a + " not handled on Arduino")
    }
  }

  /**
   * Convert sensor ID for Arduino
   * @param id Raw ID
   * @return Arduino ID
   */
  private def convertId(id:String):Int = {
    try {
      val x = Integer.parseInt(id)
      if (x>0) x
      else throw new Exception("Arduino supports only positive id for sensors")
    }
    catch {
      case e:NumberFormatException => throw new Exception("Arduino only support integer id for sensors")
    }
  }
}
