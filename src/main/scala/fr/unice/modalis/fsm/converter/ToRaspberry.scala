package fr.unice.modalis.fsm.converter

import fr.unice.modalis.fsm.core.{Node, Transition, Behavior}
import java.util.Calendar
import fr.unice.modalis.fsm.actions.unit.Result
import fr.unice.modalis.fsm.converter.transition.RaspberryTransitionTranslator
import fr.unice.modalis.fsm.converter.actions.RaspberryActionTranslator

/**
 * Created by cyrilcecchinel on 22/04/2014.
 */
object ToRaspberry extends Converter {
  val RASP_SERIAL_BAUD = 9600
  var variables:Set[Result] = Set[Result]()
  /**
   * Generate an Arduino code from a behavior
   * @param b Behavior
   * @return An arduino code
   */
  def generateCode(b:Behavior):String = {
    "\"\"\" Generated code\nDO NOT MODIFY\n" + Calendar.getInstance().getTime() + " \"\"\"\n" + generateSetup + generateLoop(b)

  }

  private def generateTransitionCode(t:Transition) = {
    "# Processing transition " + t.toString() + "\n" + RaspberryTransitionTranslator.translate(t) + "\n"
  }

  private def generateNodeCode(n:Node) = {
    val str: StringBuilder = new StringBuilder
    n.actions.actions.foreach(a => {
      val r = RaspberryActionTranslator.translate(a, variables);
      str.append(r._1);
      variables = r._2
    })
    "# Processing node " + n.name + "\n" + str.toString()
  }

  private def generateSetup= { "import serial\nser = serial.Serial('/dev/ttyUSB0',"+ RASP_SERIAL_BAUD +", timeout=1)\n"}


  def generateLoop(b:Behavior) = {
    val loop:StringBuilder = new StringBuilder

    for (i <- 0 to b.period() - 1){
      if (b.newNodeAt(i)) {
        val currentNode = b.nodeAt(i)

        // Generate actions
        loop.append(generateNodeCode(currentNode))

        // Generate transition
        loop.append(generateTransitionCode(b.transitions.filter(t => t.source == currentNode).head))
      }
    }

    "while True:\n\ttry:\n" + loop.toString() + "\texcept Exception:\n\t\tpass"
  }
}
