package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.actions.unit._
import fr.unice.modalis.cosmic.actions.guard.GuardAction
import fr.unice.modalis.cosmic.core.condition.TickCondition
import fr.unice.modalis.cosmic.actions.guard.predicate.ANDPredicate
import fr.unice.modalis.cosmic.core.{Behavior, Transition}
import fr.unice.modalis.cosmic.actions.guard.predicate.ORPredicate
import fr.unice.modalis.cosmic.actions.guard.predicate.NOTPredicate
import fr.unice.modalis.cosmic.actions.unit.EmitAction
import fr.unice.modalis.cosmic.actions.guard.constraint.{TimeConstraint, ValueConstraint}
import fr.unice.modalis.cosmic.algo.Transformation

/**
 * FIT-IoT M3 translator - Multithreaded
 */
object ToFITM3Threaded extends CodeGenerator{

  val templateFile = "embedded/python/M3FitIoTThreaded.py.template"
  val processPrefix = "process_"

  def translateAction(a:Action):(String,Set[Result]) = {
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

  def buildAction(a:Action):(String,Set[Result]) = {
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

  /**
   * Build a process into the target language
   * @param name Process name
   * @param code Code to include in the process
   * @return Process
   */
  def process_builder(name:String, code:String):String =
  {
    "def " + name + "():\n\twhile True:\n" + code + "\n"
  }

  /**
   * Build the processes header
   * @param names names of the processes
   * @return Process header
   */
  def processes_header(names:List[String]):String =
  {
    val header = new StringBuilder
    names.foreach(n => header.append(n + " = threading.Thread(None, " + n + ",\"" + n + "\")\n"))
    header.toString()
  }

  /**
   * Build autostart declaration
   * @param names Process names
   * @return Autostart declaration
   */
  def processes_autostart(names:List[String]):String =
  {
    names.map(s => s + ".start()\n").mkString


  }

  /* OVERRIDED METHODS */

  override def buildBehavior(b:Behavior):String = {
    // Get composition history for b
    val list = Transformation.getCompositionHistory(b)

    val processes = new StringBuilder
    list.foreach(b => processes.append(process_builder(processPrefix + b.id, super.buildBehavior(b))))


    processes.toString()

  }

  override def generate(b:Behavior):String = {
    val r = super.generate(b)
    val nameList = Transformation.getCompositionHistory(b).map(b => b.id).map(b => processPrefix + b)

    replace("threadsInit", processes_header(nameList) + processes_autostart(nameList), r)

  }
}
