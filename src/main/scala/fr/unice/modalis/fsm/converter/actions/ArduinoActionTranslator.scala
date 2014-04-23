package fr.unice.modalis.fsm.converter.actions

import fr.unice.modalis.fsm.actions.unit._
import fr.unice.modalis.fsm.converter.ActionTranslator
import fr.unice.modalis.fsm.actions.unit.ReadSensorAction
import fr.unice.modalis.fsm.actions.unit.SendAction
import fr.unice.modalis.fsm.actions.constraints.{ValueConstraint, Constraint}

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
      case SendAction(data, to, cl) => (buildConstraints(cl) + "Serial.println(" +  data.name + ");" +  (if (to != "") " // Send to " + to) +"\n", v)
      case ReadSensorAction(id, result, cl) => (buildConstraints(cl) + result.name + " = analogRead(" + convertId(id) + ");\n",v + result)
      case _ => throw new Exception("Action " + a + " not handled on Arduino")
    }
  }

  /**
   * Translate constraints
   * @param cl Constrains list
   * @return Constraints translated
   */
  def buildConstraints(cl:List[Constraint]):String = {
    if (cl.size > 0){

      def x(l:List[Constraint]):String = {
        l match {
          case Nil => ""
          case a::Nil => "(" + translateConstraint(a) + ")"
          case a::l => "(" + translateConstraint(a) + ") && " + x(l)
        }
      }
      "if (" + x(cl) + ")\n\t"
    }
    else
      ""
  }

  /**
   * Build Arduino semantic
   * @param c Constraint
   * @return Translated constraint
   */
  private def translateConstraint(c:Constraint):String = {
    c match {
      case ValueConstraint(value, threshold, operator) => value.name + operator + threshold
      case _ => throw new Exception("Constraint " + c + " not handled on Arduino")
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
