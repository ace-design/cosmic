import fr.unice.modalis.fsm.actions.unit._
import fr.unice.modalis.fsm.actions.unit.ReadSensorAction
import fr.unice.modalis.fsm.actions.unit.ReadSensorResult
import fr.unice.modalis.fsm.actions.unit.SendAction
import fr.unice.modalis.fsm.actions.unit.SerialInitResult
import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.converter.{ToRaspberry, ToArduino, ToGraphviz}
import fr.unice.modalis.fsm.core.{Behavior, Transition, Node}
import fr.unice.modalis.fsm.guard.constraint.ValueConstraint

/**
 * Created by cyrilcecchinel on 23/04/2014.
 */
object Demo extends App {

  /**
   * Arduino Scenarios
   */
  println("--- ARDUINO ---")

  val refRead1 = new ReadSensorResult()
  val n1 = new Node("Alice").addAction(new ReadSensorAction("1", refRead1)).addAction(new SendAction(refRead1, "Alice"))
  val t1 = new Transition(n1, n1, new TickCondition(3))
  val b1 = new Behavior(n1).addTransition(t1)

  val refRead2 = new ReadSensorResult()
  val n2 = new Node("Bob").addAction(new ReadSensorAction("1", refRead2)).addAction(new SendAction(refRead2, "Bob"))
  val t2 = new Transition(n2, n2, new TickCondition(2))
  val b2 = new Behavior(n2).addTransition(t2)

  val refRead3 = new ReadSensorResult()
  val n3 = new Node("Charlie").addAction(new ReadSensorAction("1", refRead3)).addAction(new SendAction(refRead3, "Charlie").addGuard(new ValueConstraint(refRead3, 300, "<")))
  val t3 = new Transition(n3, n3, new TickCondition(30))
  val b3 = new Behavior(n3).addTransition(t3)


  /* Single scenario : Read and send result each 3s on a Temperature Sensor*/
  println("---Scenario 1 ---")
  val scenario = b1
  /* Translate to: Graphviz */
  println(ToGraphviz(scenario))

  /* Translate to: Arduino */
  println(ToArduino(scenario))

  println("Scenario 1: Period=" + scenario.period())

  println("--- Scenario 1 + Scenario 2 ---")
  /* Add a new scenario (Read and send result each 2s on the same sensor */
  val scenario2 = b1 + b2

  /* Translate to: Graphviz */
  println(ToGraphviz(scenario2))

  /* Translate to: Arduino */
  println(ToArduino.generateCode(scenario2))

  println("Scenario 2: Period=" + scenario2.period())

  println("--- Scenario 1 + Scenario 2 + Scenario 3---")

  /* Add a new scenario (Read and send result each 6s on the same sensor */
  val scenario3 = scenario2 + b3 // b1 + b2 + b3

  /* Translate to: Graphviz */
  println(ToArduino(scenario3))

  /* Print new period */
  println("Scenario 3: Period=" + scenario3.period())

  println("--- RASPBERRY ---")

  /**
   * Raspberry Scenarios
   */

  val refSerial1 = new SerialInitResult()
  val refSerialRead1 = new ReadSerialResult()
  val rNode1 = new Node("A").addAction(new SerialInitAction("/dev/ttyUSB0", refSerial1)).addAction(new ReadSerial(refSerial1, refSerialRead1)).addAction(new EmitAction(refSerialRead1, "host", 9090))
  val rTran1 = new Transition(rNode1, rNode1, new TickCondition(1))
  val raspB1 = new Behavior(rNode1).addTransition(rTran1)

  val refSerial2 = new SerialInitResult()
  val refSerialRead2 = new ReadSerialResult()
  val rNode2 = new Node("B").addAction(new EmitAction(refSerialRead2, "i3s", 9090))
  val rTran2 = new Transition(rNode2, rNode2, new TickCondition(2))
  val raspB2 = new Behavior(rNode2).addTransition(rTran2)


  /* Translate to: Raspberry */
  println(ToRaspberry(raspB1))

  /* Translate to: Arduino : Exception */
  //println(ToArduino(raspB2))

}
