package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.actions.unit.{ReadSensorAction, WriteSerialAction, Result, Action}
import fr.unice.modalis.cosmic.core.Transition
import fr.unice.modalis.cosmic.actions.guard.GuardAction
import fr.unice.modalis.cosmic.core.condition.TickCondition
import fr.unice.modalis.cosmic.actions.guard.constraint.ValueConstraint
import fr.unice.modalis.cosmic.actions.guard.predicate.{NOTPredicate, ORPredicate, ANDPredicate}

/**
 * Created by cyrilcecchinel on 17/06/2014.
 */
object ToArduino extends CodeGenerator{

  val templateFile = "embedded/wiring/arduino.ino.template"

  def translateAction(a:Action):(String,Set[Result]) = {
     buildAction(a)
  }

  def translateTransition(t:Transition):String = {
    t.condition match {
      case TickCondition(n) => "delay(" + n * 1000 + ");\n"
      case _ => throw new Exception("Transition " + t + " not handled on Arduino plateform")
    }
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

  def buildAction(a:Action):(String,Set[Result]) = {
    a match {
      case WriteSerialAction(data, to, gl) => (buildGuards(gl) + "Serial.println(\"v=\" + String(" + data.name + "));" + (if (to != "") " // Send to " + to else ""), Set())
      case ReadSensorAction(id, result, gl) => (buildGuards(gl) + result.name + " = analogRead(" + convertId(Utils.lookupSensorAssignment(id)) + ");", Set(result))
      case _ => throw new Exception("Action " + a + " not handled on Arduino")
    }
  }

  def buildVariables():String = {
    val vari = new StringBuilder
    variables.foreach(r => vari.append("int " + r.name + ";"))

    vari.toString()
  }

  /**
   * Convert sensor ID for Arduino
   * @param id Raw ID
   * @return Arduino ID
   */
  private def convertId(id: String): Int = {
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
