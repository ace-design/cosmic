import fr.unice.modalis.cosmic.actions.guard.constraint.ValueConstraint
import fr.unice.modalis.cosmic.actions.unit.ReadSensorResult
import fr.unice.modalis.cosmic.actions.unit.ReadSensorResult
import fr.unice.modalis.cosmic.actions.unit.{EmitAction, ReadSensorAction, ReadSensorResult}
import fr.unice.modalis.cosmic.converter._
import fr.unice.modalis.cosmic.core.condition.TickCondition
import fr.unice.modalis.cosmic.core._
import fr.unice.modalis.cosmic.core.condition.TickCondition
import fr.unice.modalis.cosmic.core.Transition

/**
 * Created by cyrilcecchinel on 22/07/2014.
 */
object DemoICSR extends App {
  val pa = {
    // Variable definition
    val v_t = new ReadSensorResult()

    // Action definition
    val read = new ReadSensorAction("TEMP_SENSOR", v_t)
    val emit = new EmitAction(v_t, "alice", 8080)

    // States definition
    val s1 = new Node().addAction(read)
    val s2 = new Node().addAction(emit)

    // Transition definition
    val t1 = new Transition(s1, s2, new TickCondition(1))
    val t2 = new Transition(s2, s1, new TickCondition(1))

    // Policy definition
    new Behavior(s1).addNode(s2).addTransition(t1).addTransition(t2)
  }

  val bob_temperature = {
    // Variable definition
    val v_t = new ReadSensorResult()

    // Action definition
    val read = new ReadSensorAction("TEMP_SENSOR", v_t)
    val emit = new EmitAction(v_t, "bob", 8080)

    // State definition
    val s1 = new Node().addAction(read).addAction(emit)

    // Transition definition
    val t = new LoopTransition(s1, new TickCondition(1))

    // Policy definition
    new Behavior(s1).addTransition(t)
  }

  val bob_humidity = {
    // Variable definition
    val v_h = new ReadSensorResult()

    // Action definition
    val read = new ReadSensorAction("HUM_SENSOR", v_h)
    val emit = new EmitAction(v_h, "bob", 8080)

    // State definition
    val s1 = new Node().addAction(read).addAction(emit)
    val s2 = new Node()
    val s3 = new Node()
    // Transition definition
    val t1 = new Transition(s1, s2, new TickCondition(1))
    val t2 = new Transition(s2, s3, new TickCondition(1))
    val t3 = new Transition(s3, s1, new TickCondition(1))

    // Policy definition
    new Behavior(s1).addNodes(List(s2,s3)).addTransitions(List(t1,t2,t3))
  }

  val pb = bob_temperature + bob_humidity
  val (pamic, pabri) = pa.deploy()
  val (pbmic, pbbri) = pb.deploy()

  val pSensor_platform = pamic + pbmic
  val pBridge = pabri + pbbri

  println(ToArduino(pSensor_platform))
  println(ToRaspberryThreaded(pBridge))

}
