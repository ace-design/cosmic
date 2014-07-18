package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.actions.unit.{ReadSensorAction, WriteSerialAction, Result, Action}
import fr.unice.modalis.cosmic.core.Transition
import fr.unice.modalis.cosmic.core.condition.TickCondition

/**
 * Arduino (processing) generator
 */
object ToArduino extends ArduinoGenerator{

  val templateFile = "embedded/wiring/arduino.ino.template"


  def translateTransition(t:Transition):String = {
    t.condition match {
      case TickCondition(n) => "delay(" + n * 1000 + ");\n"
      case _ => throw new Exception("Transition " + t + " not handled on Arduino plateform")
    }
  }


  def buildAction(a:Action):(String,Set[Result]) = {
    a match {
      case WriteSerialAction(data, to, gl) => (buildGuards(gl) + "Serial.println(\"v=\" + String(" + data.name + "));" + (if (to != "") " // Send to " + to else ""), Set())
      case ReadSensorAction(id, result, gl) => (buildGuards(gl) + result.name + " = analogRead(" + convertId(Utils.lookupSensorAssignment(id)) + ");", Set(result))
      case _ => throw new Exception("Action " + a + " not handled on Arduino")
    }
  }

}
