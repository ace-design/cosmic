package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.core.Behavior
import fr.unice.modalis.cosmic.actions.unit._
import fr.unice.modalis.cosmic.algo.Transformation
import fr.unice.modalis.cosmic.actions.guard.GuardAction
import fr.unice.modalis.cosmic.actions.guard.constraint._
import fr.unice.modalis.cosmic.actions.unit.ReadSerialAction
import fr.unice.modalis.cosmic.core.Transition
import fr.unice.modalis.cosmic.actions.unit.InitSerialAction
import fr.unice.modalis.cosmic.actions.guard.predicate.ORPredicate
import fr.unice.modalis.cosmic.actions.guard.predicate.NOTPredicate
import fr.unice.modalis.cosmic.actions.unit.EmitAction
import fr.unice.modalis.cosmic.actions.guard.constraint.ValueConstraint
import fr.unice.modalis.cosmic.core.condition.TickCondition
import fr.unice.modalis.cosmic.actions.guard.predicate.ANDPredicate

/**
 * RaspberryPi translator - Multithreaded
 */
object ToRaspberryThreaded extends CodeGenerator{

  // Path to template file
  val templateFile: String = "embedded/python/raspberryThread.py.template"
  val processPrefix = "process_"
  /**
   * Translate a transition into the target language
   * @param t Transition
   * @return Transition translated
   */
  def translateTransition(t:Transition):String = //Transition translator
  {
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
  /**
   * Build an action into the target language
   * @param a Action
   * @return Action builded
   */
  def buildAction(a:Action):(String,Set[Result]) = {
    a match {
      case EmitAction(data, url, port, cl) => ("\t\t" + buildGuards(cl) + "emit("+data.name + ",\"" + url + "\"," + port + ")", Set())
      case ReadSerialAction(ref, result, cl) => ("\t\t" + buildGuards(cl) + result.name + " = " + "buffer", Set(result))
      case InitSerialAction(comPort, result, cl) => ("",Set())
      case _ => throw new Exception("Action " + a + " not handled on Raspberry")
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
    "def " + name + "():\n\twhile True:\t" + code + "\n"
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

  def buildVariables():String = {
    "" // N/A
  }

  def translateAction(a:Action):(String,Set[Result]) = {
    buildAction(a)
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
