package fr.unice.modalis.cosmic.scenario

import fr.unice.modalis.cosmic.core.Behavior

/**
 * Sensors which can be used to run scenario behaviors
 */
class Sensors(b: Behavior) {
  var behavior: Behavior = b

  def addBehavior(newb: Behavior): Unit = behavior = behavior + newb
}

class TemperatureSensor(b: Behavior) extends Sensors(b) {

}

class PollutionSensor(b: Behavior) extends Sensors(b) {

}

class PressureSensor(b: Behavior) extends Sensors(b) {

}

class SonarSensor(b: Behavior) extends Sensors(b) {

}
