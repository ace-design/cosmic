import fr.unice.modalis.cosmic.actions.guard.constraint.TimeConstraint
import fr.unice.modalis.cosmic.actions.guard.constraint.ValueConstraint
import fr.unice.modalis.cosmic.actions.guard.constraint.{TimeConstraint, ValueConstraint}
import fr.unice.modalis.cosmic.actions.guard.predicate.ANDPredicate
import fr.unice.modalis.cosmic.actions.guard.predicate.ORPredicate
import fr.unice.modalis.cosmic.actions.guard.predicate.{ANDPredicate, ORPredicate}
import fr.unice.modalis.cosmic.actions.unit.EmitAction
import fr.unice.modalis.cosmic.actions.unit.EmitAction
import fr.unice.modalis.cosmic.actions.unit.ReadSensorResult
import fr.unice.modalis.cosmic.actions.unit.ReadSensorResult
import fr.unice.modalis.cosmic.actions.unit.{EmitAction, ReadSensorAction, ReadSensorResult}
import fr.unice.modalis.cosmic.algo.Transformation
import fr.unice.modalis.cosmic.converter._
import fr.unice.modalis.cosmic.core.condition.TickCondition
import fr.unice.modalis.cosmic.core._
import fr.unice.modalis.cosmic.core.Node
import fr.unice.modalis.cosmic.core.Transition
import java.io.PrintWriter

/**
 * Created by cyrilcecchinel on 22/07/2014.
 */
object DemoICSR2 extends App{



  val s1 = {
    val v_l = new ReadSensorResult()
    val v_p = new ReadSensorResult()

    val readLight = new ReadSensorAction("LIGHT442", v_l)
    val readPresence = new ReadSensorAction("PRESENCE442", v_p)

    val emit = new EmitAction(v_p, "SECURITY_SERVER", 8080).addGuard(new ANDPredicate(new ValueConstraint(v_p, 320, ">="), new ANDPredicate(new ValueConstraint(v_p, 1, "=="), new TimeConstraint("20:00", "06:00"))))

    val n1 = new Node().addAction(readLight).addAction(readPresence).addAction(emit)


    new SimpleTemporalBehavior(n1,60)

  }

  val s2 = {
    val v_t = new ReadSensorResult()
    val readTemp = new ReadSensorAction("TEMP442", v_t)
    val emit = new EmitAction(v_t, "INSPECTOR_SERVER", 8080).addGuard(new ValueConstraint(v_t, 50, ">="))

    val n1 = new Node().addAction(readTemp).addAction(emit)

    new SimpleTemporalBehavior(n1,30)
  }

  val s3 = {
    val v_t = new ReadSensorResult()

    val read = new ReadSensorAction("TEMP442", v_t)
    val emit = new EmitAction(v_t, "CAMPUS_MANAGER_SERVER", 8080).addGuard(new ORPredicate(
      new ValueConstraint(v_t, 24, ">"), new ValueConstraint(v_t, 16, "<")))

    val n1 = new Node().addAction(read).addAction(emit)

    new SimpleTemporalBehavior(n1, 150)
  }

  val s4 = {
    val v_l = new ReadSensorResult()
    val v_p = new ReadSensorResult()

    val readLight = new ReadSensorAction("LIGHT442", v_l)
    val readPresence = new ReadSensorAction("PRESENCE442", v_p)

    val emit = new EmitAction(v_p, "QUALITY_SERVER", 8080).addGuard(new ANDPredicate(new ValueConstraint(v_p, 320, ">="), new ANDPredicate(new ValueConstraint(v_p, 0, "=="), new TimeConstraint("20:00", "06:00"))))

    val n1 = new Node().addAction(readLight).addAction(readPresence).addAction(emit)


    new SimpleTemporalBehavior(n1,300)

  }

  val (s1Sp, s1Br) = s1.deploy()
  val (s2Sp, s2Br) = s2.deploy()
  val (s3Sp, s3Br) = s3.deploy()
  val (s4Sp, s4Br) = s4.deploy()

  val spPolicies = Seq(s1Sp, s2Sp, s3Sp, s4Sp)
  val brPolicies = Seq(s1Br, s2Br, s3Br, s4Br)

  fr.unice.modalis.cosmic.converter.Utils.writefile("arduinonativeS1", ToArduino(s1Sp))
  fr.unice.modalis.cosmic.converter.Utils.writefile("arduinonativeS2", ToArduino(s2Sp))
  fr.unice.modalis.cosmic.converter.Utils.writefile("arduinonativeS3", ToArduino(s3Sp))
  fr.unice.modalis.cosmic.converter.Utils.writefile("arduinonativeS4", ToArduino(s4Sp))

  fr.unice.modalis.cosmic.converter.Utils.writefile("arduinocontikiS1", ToArduino(s1Sp))
  fr.unice.modalis.cosmic.converter.Utils.writefile("arduinocontikiS2", ToArduino(s2Sp))
  fr.unice.modalis.cosmic.converter.Utils.writefile("arduinocontikiS3", ToArduino(s3Sp))
  fr.unice.modalis.cosmic.converter.Utils.writefile("arduinocontikiS4", ToArduino(s4Sp))

  fr.unice.modalis.cosmic.converter.Utils.writefile("raspberryS1", ToRaspberryThreaded(s1Br))
  fr.unice.modalis.cosmic.converter.Utils.writefile("raspberryS2", ToRaspberryThreaded(s2Br))
  fr.unice.modalis.cosmic.converter.Utils.writefile("raspberryS3", ToRaspberryThreaded(s3Br))
  fr.unice.modalis.cosmic.converter.Utils.writefile("raspberryS4", ToRaspberryThreaded(s4Br))

  fr.unice.modalis.cosmic.converter.Utils.writefile("M3S1", ToFITM3Threaded(s1))
  fr.unice.modalis.cosmic.converter.Utils.writefile("M3S2", ToFITM3Threaded(s2))
  fr.unice.modalis.cosmic.converter.Utils.writefile("M3S3", ToFITM3Threaded(s3))
  fr.unice.modalis.cosmic.converter.Utils.writefile("M3S4", ToFITM3Threaded(s4))

  val composedSp = s1Sp + s2Sp + s3Sp + s4Sp
  val composedBr = s1Br + s2Br + s3Br + s4Br
  val composed = s1 + s2 + s3 + s4

  fr.unice.modalis.cosmic.converter.Utils.writefile("CompoArduinoNative", ToArduino(composedSp))
  fr.unice.modalis.cosmic.converter.Utils.writefile("CompoArduinoContiki", ToArduinoContiki(composedSp))
  fr.unice.modalis.cosmic.converter.Utils.writefile("CompoRaspberry", ToRaspberryThreaded(composedBr))
  fr.unice.modalis.cosmic.converter.Utils.writefile("CompoM3", ToFITM3Threaded(composed))


  /*val spFinal = s1SP + s2SP + s3SP
  println(Transformation.getCompositionHistory(spFinal))

  println(" --- Code generation ---")
  println("\t * Arduino contiki")
  fr.unice.modalis.cosmic.converter.Utils.writefile("arduinocontiki", ToArduinoContiki(spFinal))
  println("\t * Arduino native")
  fr.unice.modalis.cosmic.converter.Utils.writefile("arduinonative", ToArduino(spFinal))
  //fr.unice.modalis.cosmic.converter.Utils.writefile("arduinocontiki", ToArduinoContiki((s1 + s2 + s3).deploy()._1))*/




}

