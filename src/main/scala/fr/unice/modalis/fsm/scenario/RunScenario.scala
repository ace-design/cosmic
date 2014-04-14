package fr.unice.modalis.fsm.scenario
import fr.unice.modalis.fsm.converter.ToGraphviz


object RunScenario extends App {

  // Build a Temperature sensor and initiate scenario on it
 /* val temperatureSensor = new TemperatureSensor(HeatingMonitoring.init())
  temperatureSensor.addBehavior(EnergyLoss.init())



  // Build a pressure sensor and initiate scernario on it
  val pressureSensor = new PressureSensor(IntrusionPrevention.init())
  pressureSensor.addBehavior(WindowOpening.init()) */

  // Build a Pollution sensor and initiate scenario on it
  val pollutionSensor = new PollutionSensor(AirQuality.init())
  pollutionSensor.addBehavior(CarPooling.init())

  // Generate Graphviz code
  println(ToGraphviz.generateCode(pollutionSensor.behavior))
  
}