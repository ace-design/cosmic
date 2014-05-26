package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.core.{Node, Transition, Behavior}
import java.util.Calendar
import fr.unice.modalis.cosmic.actions.unit.Result
import fr.unice.modalis.cosmic.converter.transition.RaspberryTransitionTranslator
import fr.unice.modalis.cosmic.converter.actions.RaspberryActionTranslator
import scala.io.Source

/**
 * Created by cyrilcecchinel on 22/04/2014.
 */
object ToRaspberry extends Converter {
  val RASP_SERIAL_BAUD = 9600
  var variables: Set[Result] = Set[Result]()

  /**
   * Generate an Arduino code from a behavior
   * @param b Behavior
   * @return An arduino code
   */
  def generateCode(b: Behavior): String = {
    val str = new StringBuilder
    str.append("\"\"\" Generated code\nDO NOT MODIFY\n" + Calendar.getInstance().getTime() +"\n" +"\"\"\"\n")

    val behaviorCode = generateLoop(b)

    val fromTemplate = Source.fromFile("embedded/python/main.py.template").getLines().mkString("\n").replace("#@code@#",behaviorCode)

    str.append(fromTemplate)
    str.toString()

  }

  private def generateTransitionCode(t: Transition) = {
    "# Processing transition " + t.toString() + "\n" + RaspberryTransitionTranslator.translate(t) + "\n"
  }

  private def generateNodeCode(n: Node) = {
    val str: StringBuilder = new StringBuilder
    n.actions.actions.foreach(a => {
      val r = RaspberryActionTranslator.translate(a, variables);
      str.append(r._1);
      variables = r._2
    })
    "# Processing node " + n.name + "\n" + str.toString()
  }

  private def generateSetup = {
    ""
  }


  def generateLoop(b: Behavior) = {
    val loop: StringBuilder = new StringBuilder

    if (b.period() == 0) {
      b.nodes.foreach(n => loop.append(generateNodeCode(n)))
    } else {
      for (i <- 0 to b.period() - 1) {
        if (b.newNodeAt(i)) {
          val currentNode = b.nodeAt(i)

          // Generate actions
          loop.append(generateNodeCode(currentNode))

          // Generate transition
          loop.append(generateTransitionCode(b.transitions.filter(t => t.source == currentNode).head))
        }
      }
    }
    "\twhile True:\n" + loop.toString()
  }
}
