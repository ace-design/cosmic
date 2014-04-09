package fr.unice.modalis.fsm.scenario
import fr.unice.modalis.fsm.converter.ToGraphviz


object RunScenario extends App {

  // Build a Temperature sensor and initiate scenarios on it
  val temperatureSensor = new TemperatureSensor(HeatingMonitoring.init())
  temperatureSensor.addBehavior(EnergyLoss.init())

  // Build a Pollution sensor and initiate scenarios on it
  val pollutionSensor = new PollutionSensor(AirQuality.init())
  pollutionSensor.addBehavior(CarPooling.init())

  // Generate Graphviz code
  println(ToGraphviz.generateCode(temperatureSensor.behavior))
  println(ToGraphviz.generateCode(pollutionSensor.behavior))
  
}