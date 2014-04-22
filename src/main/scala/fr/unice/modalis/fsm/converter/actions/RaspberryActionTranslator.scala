package fr.unice.modalis.fsm.converter.actions

import fr.unice.modalis.fsm.actions.unit._
import fr.unice.modalis.fsm.converter.ActionTranslator

/**
 * Translate actions for Raspberry Pi
 */
object RaspberryActionTranslator extends ActionTranslator{

  /**
   * Translate an action
   * @param a action Action
   * @param v Current variables set
   * @return Translated action and new variables set
   */

  def translate(a:Action,v:Set[Result]):(String,Set[Result]) = {
    a match {
      case EmitAction(data, url, port, _) => ("\tprint(\"Emitting \"+ "+ (if (data==null) "Nothing" else data.name) + " +\" to " + url + ":" + port + ")\"\n", v)
      case ReadSerial(ref, result, _) => ("\t"+ result.name + " = " + ref.name + ".readline()\n",v + result)
      case SerialInit(comPort, result, _) => ("\t"+ result.name + "= serial.Serial(\'"+ comPort + "\', 9600, timeout=1)\n",v + result)
      case _ => throw new Exception("Action " + a + " not handled on Raspberry")
    }
  }
}