package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.actions.guard.GuardAction
import fr.unice.modalis.cosmic.actions.guard.constraint.{TimeConstraint, ValueConstraint}
import fr.unice.modalis.cosmic.actions.guard.predicate.{ANDPredicate, NOTPredicate, ORPredicate}
import fr.unice.modalis.cosmic.actions.unit.{EmitAction, _}
import fr.unice.modalis.cosmic.core.Transition
import fr.unice.modalis.cosmic.core.condition.TickCondition

/**
 * FIT-IoT M3 translator
 */
object ToFITM3 extends CodeGenerator{
  val templateFile = "embedded/python/M3FitIoT.py.template"

  def translateAction(a:Action):(String,Set[Variable]) = {
    buildAction(a)
  }

  def translateTransition(t:Transition):String = {
    t.condition match {
      case TickCondition(n) => indent(4) + "time.sleep(" + n + ");"
      case _ => throw new Exception("Transition " + t + " not handled on FIT M3 platform")
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
      case _ => throw new Exception("Guard " + g + " not handled on FIT M3 platform")
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
      "if " + x(l) + ":\n" + indent(5)
    }
    else
      ""
  }

  def buildAction(a:Action):(String,Set[Variable]) = {
    a match {
      case EmitAction(data, url, port, cl) => (indent(4) + buildGuards(cl) + "emit("+data.name + ",\"" + url + "\"," + port + ")", Set())
      case ReadSensorAction(id, result, gl) => (indent(4) + buildGuards(gl) + result.name + " = get_measure(\"" + Utils.lookupSensorAssignment(id) + "\");", Set(result))
      case _ => throw new Exception("Action " + a + " not handled on FIT M3 platform")
    }
  }

  def buildVariables():String = {
    "" // N/A
  }

  private def indent(i:Int):String = {
    i match {
      case i if i>0 => "   " + indent(i-1)
      case _ => ""
    }

  }
}
