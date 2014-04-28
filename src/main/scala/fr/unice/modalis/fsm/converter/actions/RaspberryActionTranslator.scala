package fr.unice.modalis.fsm.converter.actions

import fr.unice.modalis.fsm.actions.unit._
import fr.unice.modalis.fsm.converter.ActionTranslator
import fr.unice.modalis.fsm.actions.constraints._
import fr.unice.modalis.fsm.actions.unit.ReadSerial
import fr.unice.modalis.fsm.actions.unit.SerialInitAction
import fr.unice.modalis.fsm.actions.constraints.ANDConstraint
import fr.unice.modalis.fsm.actions.constraints.ORConstraint
import fr.unice.modalis.fsm.actions.unit.EmitAction
import fr.unice.modalis.fsm.actions.constraints.ValueConstraint

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
      case EmitAction(data, url, port, cl) => ("\t\t" + buildConstraints(cl) + "print(\"Emitting \"+ "+ (if (data==null) "Nothing" else "str(" + data.name +")") + " +\" to " + url + ":" + port + "\")\n", v)
      case ReadSerial(ref, result, cl) => ("\t\t" + buildConstraints(cl) + result.name + " = " + ref.name + ".readline()\n",v + result)
      case SerialInitAction(comPort, result, cl) => ("\t\t" + buildConstraints(cl) + result.name + "= serial.Serial(\'"+ comPort + "\', 9600, timeout=1)\n",v + result)
      case _ => throw new Exception("Action " + a + " not handled on Raspberry")
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
          case a::l => translateConstraint(a) + " and " + x(l)
        }
      }
      "if " + x(cl) + ":\n\t\t\t"
    }
    else
      ""
  }

  /**
   * Build Python semantic
   * @param c Constraint
   * @return Translated constraint
   */
  private def translateConstraint(c:Constraint):String = {
    c match {
      case ValueConstraint(value, threshold, operator) => "int(" + value.name + ")" + operator + threshold
      case ANDConstraint(left, right) => translateConstraint(left) + " and " + translateConstraint(right)
      case ORConstraint(left, right) => translateConstraint(left) + " or " + translateConstraint(right)
      case NOTConstraint(exp) => "not " + translateConstraint(exp)
      case _ => throw new Exception("Constraint " + c + " not handled on Raspberry")
    }
  }
}