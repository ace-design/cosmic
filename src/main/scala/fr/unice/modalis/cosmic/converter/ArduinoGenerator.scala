package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.actions.guard.GuardAction
import fr.unice.modalis.cosmic.actions.guard.constraint.ValueConstraint
import fr.unice.modalis.cosmic.actions.guard.predicate.{ANDPredicate, NOTPredicate, ORPredicate}
import fr.unice.modalis.cosmic.actions.unit.{Action, Variable}

/**
 * Common methods for Arduino code generation
 */
trait ArduinoGenerator extends CodeGenerator{
  def translateAction(a:Action):(String,Set[Variable]) = {
    buildAction(a)
  }

  def translateGuard(g:GuardAction):String = {
    g match {
      case ValueConstraint(value, threshold, operator) => value.name + operator + threshold
      case ANDPredicate(left, right) => translateGuard(left) + " && " + translateGuard(right)
      case ORPredicate(left, right) => translateGuard(left) + " || " + translateGuard(right)
      case NOTPredicate(exp) => "!" + translateGuard(exp)
      case _ => throw new Exception("Guard " + g + " not handled on Arduino")
    }
  }


  def buildGuards(l:List[GuardAction]):String = {
    if (l.size> 0) {

      def x(l: List[GuardAction]): String = {
        l match {
          case Nil => ""
          case a :: Nil => "(" + translateGuard(a) + ")"
          case a :: l => "(" + translateGuard(a) + ") && " + x(l)
        }
      }
      "if (" + x(l) + ")\n\t"
    }
    else
      ""
  }

  /**
   * Build variable list into the target language
   * @return Variable list
   */
  def buildVariables():String = //Variables builder
  {
    val vari = new StringBuilder
    variables.foreach(r => vari.append("int " + r.name + ";"))

    vari.toString()
  }


  /**
   * Convert sensor ID for Arduino
   * @param id Raw ID
   * @return Arduino ID
   */
   def convertId(id: String): Int = {
    try {
      val x = Integer.parseInt(id)
      if (x > 0) x
      else throw new Exception("Arduino supports only positive id for sensors")
    }
    catch {
      case e: NumberFormatException => throw new Exception("Arduino only support integer id for sensors")
    }
  }
}
