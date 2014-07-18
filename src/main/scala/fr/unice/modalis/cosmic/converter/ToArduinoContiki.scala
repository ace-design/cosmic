package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.actions.unit.{ReadSensorAction, WriteSerialAction, Result, Action}
import fr.unice.modalis.cosmic.core.{Behavior, Transition}
import fr.unice.modalis.cosmic.core.condition.TickCondition
import fr.unice.modalis.cosmic.algo.Transformation

/**
 * To Arduino (Contiki generator)
 */
object ToArduinoContiki extends ArduinoGenerator{

  // Path to template file
  val templateFile: String = "embedded/C/contikiarduino.c.template"


  /**
   * Translate a transition into the target language
   * @param t Transition
   * @return Transition translated
   */
  def translateTransition(t:Transition):String = //Transition translator
  {
    t.condition match {
      case TickCondition(n) => "etimer_set(&timer, CLOCK_CONF_SECOND * " + n + ");\nPROCESS_WAIT_EVENT();"
    }
  }


  /**
   * Build an action into the target language
   * @param a Action
   * @return Action builded
   */
  def buildAction(a:Action):(String,Set[Result]) = //Action builder (ie. Action + guard)
  {
    a match {
      case WriteSerialAction(data, to, gl) => (buildGuards(gl) + "printf(\"%d\"," + data.name + ");" + (if (to != "") " // Send to " + to else ""), Set())
      case ReadSensorAction(id, result, gl) => (buildGuards(gl) + result.name + " = readSensor(" + convertId(Utils.lookupSensorAssignment(id)) + ");", Set(result))
      case _ => throw new Exception("Action " + a + " not handled on Arduino")
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
      "PROCESS_THREAD(" + name + ", ev, data){\nstatic struct etimer timer;\nPROCESS_BEGIN();\nwhile(1){\n" + code + "}\nPROCESS_END();\n}\n"
  }

  /**
   * Build the processes header
   * @param names names of the processes
   * @return Process header
   */
  def processes_header(names:List[String]):String =
  {
    val header = new StringBuilder
    names.foreach(n => header.append("PROCESS(" + n + ", \"" + n + " process\");\n"))
    header.toString()
  }

  /**
   * Build autostart declaration
   * @param names Process names
   * @return Autostart declaration
   */
  def processes_autostart(names:List[String]):String =
  {

    "AUTOSTART_PROCESSES(" + names.map(s => "&"+s).mkString(",") + ");\n"
  }

  /* OVEERIDED METHODS */

  override def buildBehavior(b:Behavior):String = {
    // Get composition history for b
    val list = Transformation.getCompositionHistory(b)

    val processes = new StringBuilder
    list.foreach(b => processes.append(process_builder(b.id, super.buildBehavior(b))))


    processes.toString()

  }

  override def generate(b:Behavior):String = {
    val r = super.generate(b)
    val nameList = Transformation.getCompositionHistory(b).map(b => b.id)

    replace("threadsInit", processes_header(nameList) + processes_autostart(nameList), r)

  }



}
