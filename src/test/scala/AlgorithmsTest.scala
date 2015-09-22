import fr.unice.modalis.cosmic.actions.unit._
import fr.unice.modalis.cosmic.algo.vm.VirtualMachine
import fr.unice.modalis.cosmic.algo.{Transformation, Utils}
import fr.unice.modalis.cosmic.core.condition.TickCondition
import fr.unice.modalis.cosmic.core.{Behavior, Node, Transition}
import fr.unice.modalis.cosmic.scenario.{AirQuality, CarPooling, HeatingMonitoring}
import org.specs2.mutable.SpecificationWithJUnit

/**
 * Created by cyrilcecchinel on 14/04/2014.
 */
class AlgorithmsTest extends SpecificationWithJUnit {
  def alice():Behavior = {
    // Alice whishes to read and receive temperature measures every two seconds
    val v_t = new ReadSensorVariable()

    val read = new ReadSensorAction("TEMP", v_t)
    val emit = new EmitAction(v_t, "alice", 8080)

    val n = new Node("Alice").addAction(read).addAction(emit)
    val t = new Transition(n,n, new TickCondition(2))

    new Behavior(n).addTransition(t)
  }

  def bob():Behavior = {
    // Bob whishes to read and receive temperature measures every three seconds
    val v_t = new ReadSensorVariable()

    val read = new ReadSensorAction("TEMP", v_t)
    val emit = new EmitAction(v_t, "bob", 2525)

    val n = new Node("Bob").addAction(read).addAction(emit)
    val t = new Transition(n,n, new TickCondition(3))

    new Behavior(n).addTransition(t)
  }

  def charlie():Behavior = {
    // Bob whishes to read and receive temperature measures every three seconds
    val v_t = new ReadSensorVariable()

    val read = new ReadSensorAction("TEMP", v_t)
    val emit = new EmitAction(v_t, "charlie", 2525)

    val n = new Node("charlie").addAction(read).addAction(emit)
    val t = new Transition(n,n, new TickCondition(10))

    new Behavior(n).addTransition(t)
  }

  "A development must maintain behavior properties (correctness and period)" in {
    val n1: Node = new Node("A");
    val n2: Node = new Node("B");
    val n3: Node = new Node("C");
    n3.addAction(new EmitAction("a", 0))

    val t1: Transition = new Transition(n1, n2, new TickCondition(3))
    val t2: Transition = new Transition(n2, n3, new TickCondition(2))
    val t3: Transition = new Transition(n3, n1, new TickCondition(1))

    val b: Behavior = new Behavior(n1).addNodes(List[Node](n2, n3)).addTransitions(List[Transition](t1, t2, t3))

    val dv: Behavior = VirtualMachine.apply(b, Transformation.develop(b))

    (Utils.isCorrectBehavior(dv) mustEqual (true)) and (b.period() must_== dv.period())
  }

  "A factorization must maintain behavior properties (correctness and period)" in {
    val n1: Node = new Node("A");
    val n2: Node = new Node("B");
    val n3: Node = new Node("C");
    n3.addAction(new EmitAction("a", 0))

    val t1: Transition = new Transition(n1, n2, new TickCondition(1))
    val t2: Transition = new Transition(n2, n3, new TickCondition(1))
    val t3: Transition = new Transition(n3, n1, new TickCondition(1))

    val b: Behavior = new Behavior(n1).addNodes(List[Node](n2, n3)).addTransitions(List[Transition](t1, t2, t3))

    val fb: Behavior = VirtualMachine.apply(b, Transformation.factorize(b))

    (Utils.isCorrectBehavior(fb) mustEqual (true)) and (b.period() must_== fb.period())
  }

  "A simple composition must maintain behavior properties (correctness and period = lcm(overall perdiods))" in {
    val a = AirQuality.init()
    val b = CarPooling.init()
    val composed = a + b



    (Utils.isCorrectBehavior(composed) mustEqual (true)) and (composed.period() must_== Utils.lcm(a.period(), b.period()))
  }

  "A complex composition must maintain behavior properties (correctness and period = lcm(overall perdiods))" in {
    val a = AirQuality.init()
    val b = CarPooling.init()
    val c = HeatingMonitoring.init()
    val composed = a + b + c

    (Utils.isCorrectBehavior(composed) mustEqual (true)) and (composed.period() must_== Utils.lcmm(List[Int](a.period(), b.period(), c.period())))
  }

  "A composition returns a valid behavior" in {
    /* Behavior 1 */
    val n1: Node = new Node("B").addAction(new EmitAction("host", 9090))
    val n2: Node = new Node("Bnull1")
    val n3: Node = new Node("Bnull2")

    val t1: Transition = new Transition(n1, n2, new TickCondition(1))
    val t2: Transition = new Transition(n2, n3, new TickCondition(1))
    val t3: Transition = new Transition(n3, n1, new TickCondition(1))

    val behavior: Behavior = new Behavior(n1).addNodes(List[Node](n1, n2, n3)).addTransitions(List[Transition](t1, t2, t3))

    /* Behavior 2 */
    val m1: Node = new Node("A").addAction(new EmitAction("office", 4242))
    val m2: Node = new Node("Anull1")
    val u1: Transition = new Transition(m1, m2, new TickCondition(1))
    val u2: Transition = new Transition(m2, m1, new TickCondition(1))

    val behavior2: Behavior = new Behavior(m1).addNode(m1).addNode(m2).addTransition(u1).addTransition(u2)

    /* Behavior 3 */
    val o1: Node = new Node("C").addAction(new EmitAction("i3s", 1212))
    val v1 = new Transition(o1, o1, new TickCondition(4))
    val behavior3: Behavior = new Behavior(o1).addNode(o1).addTransition(v1)

    val composed = behavior + behavior2 + behavior3

    /* Properties */
    val composedPeriod = Utils.lcmm(List[Int](behavior.period(), behavior2.period(), behavior3.period()))
    val numberOfNodes = composed.nodes.size
    val numberOfTransitions = composed.transitions.size
    val validity = Utils.isCorrectBehavior(composed)
    val checkActions = (composed.nodeAt(2).actions.actions.toSet.equals(Set[Action](new EmitAction("office", 4242)))
      && composed.nodeAt(6).actions.actions.toSet.equals(Set[Action](new EmitAction("office", 4242), new EmitAction("host", 9090)))
      && composed.nodeAt(8).actions.actions.toSet.equals(Set[Action](new EmitAction("office", 4242), new EmitAction("i3s", 1212))))
    /* Assumptions */
    (composedPeriod must_== 12) && (numberOfNodes must_== 8) && (numberOfTransitions must_== 8) && (validity mustEqual (true)) && (checkActions mustEqual (true))

  }


  "Idempotance" in {
    val rNode1 = new Node("A").addAction(new EmitAction("A", 0))
    val rTran1 = new Transition(rNode1, rNode1, new TickCondition(1))
    val asynch = new Behavior(rNode1).addTransition(rTran1)

    (asynch + asynch) must_== (asynch)
  }

  "A composition should be historized (1)" in {
    val a = alice(); val b = bob(); val c = charlie();

    val result = a + b + c
    val history = Transformation.getCompositionHistory(result)

    (history.size must_== 3)
  }

  "A composition should be historized (2)" in {
    val a = alice(); val b = bob(); val c = charlie();

    val result = a + b + c
    val history = Transformation.getCompositionHistory(result)

    (history.contains(a) && history.contains(b) && history.contains(c)) must_== true
  }

  "A composition should be historized (3)" in {
    val a = alice();

    val history = Transformation.getCompositionHistory(a)

    (history.size must_== 1) && (history.contains(a))
  }
}
