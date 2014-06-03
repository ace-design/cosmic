package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.core.{Transition, Node, Behavior}
import fr.unice.modalis.cosmic.converter.actions.ArduinoActionTranslator
import fr.unice.modalis.cosmic.converter.transition.ArduinoTransitionTranslator
import fr.unice.modalis.cosmic.actions.unit.Result
import java.util.Calendar
import scala.io.Source

/**
 * Arduino translator
 */
object ToArduino extends Converter {
  val ARDUINO_SERIAL_BAUD = 9600
  var variables: Set[Result] = Set[Result]()

  /**
   * Generate an Arduino code from a behavior
   * @param b Behavior
   * @return An arduino code
   */
  def generateCode(b: Behavior): String = {
    val str = new StringBuilder

    str.append("/* Generated code\nDO NOT MODIFY\n" + Calendar.getInstance().getTime() + " */\n")

    val behaviorCode = generateLoop(b)

    val fromTemplate = Source.fromFile("embedded/wiring/main.ino.template").getLines().mkString("\n").replace("#@code@#",behaviorCode)

    str.append(fromTemplate)
    str.toString()

  }

  private def generateNodeCode(n: Node) = {
    val str: StringBuilder = new StringBuilder
    n.actions.actions.foreach(a => {
      val r = ArduinoActionTranslator.translate(a, variables);
      str.append(r._1);
      variables = r._2
    })
    "// Processing node " + n.name + "\n" + str.toString()
  }

  private def generateTransitionCode(t: Transition) = {
    "// Processing transition " + t.toString() + "\n" + ArduinoTransitionTranslator.translate(t) + "\n"
  }

  def generateLoop(b: Behavior) = {
    val loop: StringBuilder = new StringBuilder

    for (i <- 0 to b.period() - 1) {
      if (b.newNodeAt(i)) {
        val currentNode = b.nodeAt(i)

        // Generate actions
        loop.append(generateNodeCode(currentNode))

        // Generate transition
        loop.append(generateTransitionCode(b.transitions.filter(t => t.source == currentNode).head))
      }
    }

    // Build variables definition
    val vari = new StringBuilder
    variables.foreach(r => vari.append("int " + r.name + ";"))

    vari.toString() + "\n" + loop.toString()
  }
}
