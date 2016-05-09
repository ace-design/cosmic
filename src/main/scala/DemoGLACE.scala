import fr.unice.modalis.cosmic.actions.guard.constraint.ValueConstraint
import fr.unice.modalis.cosmic.actions.unit.{EmitAction, ReadSensorAction, ReadSensorVariable}
import fr.unice.modalis.cosmic.converter.{ToArduino, ToRaspberry}
import fr.unice.modalis.cosmic.core.{Behavior, Node, SimpleTemporalBehavior}

/**
  * This is an implementation of the scenario presented during the GL/\CE Tools day @Paris
  * It presents three users with different needs:
  *   * Alice: Read/Receive temperature values every 2 seconds
  *   * Bob: Read/Receive temperature values every 3 seconds
  *   * Charlie: Read/Receive humidity values every minute
  *
  * The needs are translated into a data collection policy (ie A Behavior extending DemoPolicy for the
  * purpose of the demo) and then composed in a single data collection policy.
  * The result is then decomposed to fit the infrastructure facilities and Processing/Python codes are generated
  *
  * Created by Cyril Cecchinel - I3S Laboratory on 08/05/2016.
  */

/**
  * A trait adding syntactic sugar for the demo purposes
  */
trait DemoPolicy  {
  val policy:Behavior

  def apply() = policy
}

/**
  * Alice data collection policy
  */
object Alice extends DemoPolicy{

  val read_return = ReadSensorVariable("v")
  val read_action = ReadSensorAction("TEMP_SENSOR", read_return, List())

  val sendAction = EmitAction(read_return, "alice", 8080, List())

  val policy = new SimpleTemporalBehavior(new Node().addAction(read_action).addAction(sendAction), 2)
}

/**
  * Bob data collection policy
  */
object Bob extends DemoPolicy{

  val read_return = ReadSensorVariable("v")
  val read_action = ReadSensorAction("TEMP_SENSOR", read_return, List())

  val sendAction = EmitAction(read_return, "bob", 8080, List())

  val policy = new SimpleTemporalBehavior(new Node().addAction(read_action).addAction(sendAction), 3)
}

/**
  * Charlie data collection policy
  */
object Charlie extends DemoPolicy{

  val read_return = ReadSensorVariable("v")
  val read_action = ReadSensorAction("HUM_SENSOR", read_return, List(ValueConstraint(read_return, 75, ">")))

  val sendAction = EmitAction(read_return, "bob", 8080, List())

  val policy = new SimpleTemporalBehavior(new Node().addAction(read_action).addAction(sendAction), 60)
}

/**
  * Demo
  */
object DemoGLACE extends App{

  // We print the period of the different policies
  println(Alice().period())
  println(Bob().period())
  println(Charlie().period())

  // Check that the composed period is equal to LCM(Alice.period, Bob.period) = LCM(2,3) = 6
  println((Alice() + Bob()).period())


  // Composing the global policy (Alice + Bob) + Charlie
  val composed = Alice() + Bob() + Charlie()

  // Decomposing the global policy to fit the sensing platform and the bridge
  val (pSensors, pBridge) = composed.deploy()


  // Displaying the decomposed policies
  println(pSensors)
  println(pBridge)

  // Generating code
  println(ToArduino(pSensors))
  println(ToRaspberry(pBridge))

}
