package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.actions.unit._
import fr.unice.modalis.cosmic.actions.guard.GuardAction
import fr.unice.modalis.cosmic.actions.unit.ReadSerialAction
import fr.unice.modalis.cosmic.core.condition.TickCondition
import fr.unice.modalis.cosmic.actions.guard.predicate.ANDPredicate
import fr.unice.modalis.cosmic.core.Transition
import fr.unice.modalis.cosmic.actions.guard.predicate.ORPredicate
import fr.unice.modalis.cosmic.actions.guard.predicate.NOTPredicate
import fr.unice.modalis.cosmic.actions.unit.EmitAction
import fr.unice.modalis.cosmic.actions.guard.constraint.{TimeConstraint, ValueConstraint}

/**
 * RaspberryPi translator
 */
object ToRaspberry extends CodeGenerator{

  val templateFile = "embedded/python/raspberry.py.template"
  val processPrefix = "process_"

  def translateAction(a:Action):(String,Set[Result]) = {
    buildAction(a)
  }

  def translateTransition(t:Transition):String = {
    t.condition match {
      case TickCondition(n) => "\t\ttime.sleep(" + n + ");\n"
      case _ => throw new Exception("Transition " + t + " not handled on Raspberry plateform")
    }
  }

  def translateGuard(g:GuardAction):String = {
    g match {
      case ValueConstraint(value, threshold, operator) => "int(" + value.name + ")" + operator + threshold
      case TimeConstraint(begin, end) => {
        val time1 = "datetime.time(" + begin.getHourOfDay + "," + begin.getMinuteOfHour + ", 0)"
        val time2 = "datetime.time(" + end.getHourOfDay + "," + end.getMinuteOfHour + ", 0)"
        "checkTime(" + time1 + "," + time2 +")"
      }
      case ANDPredicate(left, right) => translateGuard(left) + " and " + translateGuard(right)
      case ORPredicate(left, right) => translateGuard(left) + " or " + translateGuard(right)
      case NOTPredicate(exp) => "not " + translateGuard(exp)
      case _ => throw new Exception("Guard " + g + " not handled on Raspberry plateform")
    }
  }


  def buildGuards(l:List[GuardAction]):String = {
    if (l.size> 0) {

      def x(l: List[GuardAction]): String = {
        l match {
          case Nil => ""
          case a :: Nil => "(" + translateGuard(a) + ")"
          case a :: l => translateGuard(a) + " and " + x(l)
        }
      }
      "if " + x(l) + ":\n\t\t\t"
    }
    else
      ""
  }

  def buildAction(a:Action):(String,Set[Result]) = {
    a match {
      case EmitAction(data, url, port, cl) => ("\t\t" + buildGuards(cl) + "emit("+data.name + ",\"" + url + "\"," + port + ")", Set())
      case ReadSerialAction(ref, result, cl) => ("\t\t" + buildGuards(cl) + result.name + " = " + "buffer", Set(result))
      case InitSerialAction(comPort, result, cl) => ("",Set())
      case _ => throw new Exception("Action " + a + " not handled on Raspberry")
    }
  }

  def buildVariables():String = {
   "" // N/A
  }


}
