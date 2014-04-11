import fr.unice.modalis.fsm.algo.Utils
import fr.unice.modalis.fsm.condition.TrueCondition
import fr.unice.modalis.fsm.core.{SimpleTemporalBehavior, Behavior, Transition, Node}
import org.specs2.mutable.Specification

/**
 * Created by cyrilcecchinel on 11/04/2014.
 */
class UtilsTest extends Specification{
  "A util class" should {
    "provide cycle detection algorithm (correct behavior)" in {
      val a = new Node("A")
      val b = new Node("B")
      val c = new Node("C")

      val t1 = new Transition(a,a, new TrueCondition)
      val t2 = new Transition(a,b, new TrueCondition)
      val t3 = new Transition(a,c, new TrueCondition)
      val t4 = new Transition(b,b, new TrueCondition)
      val t5 = new Transition(b,c, new TrueCondition)
      val t6 = new Transition(c,a, new TrueCondition)
      val behavior = new Behavior(a, Set[Node](a,b,c), Set[Transition](t1,t2,t3,t4,t5,t6))

      Utils.checkCycle(behavior) must equalTo(true)
    }
    "provide cycle detection algorithm (behavior with a well-state)" in {
      val a = new Node("A")
      val b = new Node("B")
      val c = new Node("C")
      val d = new Node("D")

      val t1 = new Transition(a,a, new TrueCondition)
      val t2 = new Transition(a,b, new TrueCondition)
      val t3 = new Transition(a,c, new TrueCondition)
      val t4 = new Transition(b,b, new TrueCondition)
      val t5 = new Transition(b,c, new TrueCondition)
      val t6 = new Transition(c,a, new TrueCondition)
      val t7 = new Transition(c,d, new TrueCondition)
      val behavior = new Behavior(a, Set[Node](a,b,c,d), Set[Transition](t1,t2,t3,t4,t5,t6,t7))

      Utils.checkCycle(behavior) must equalTo(false)
    }

    "provide cycle detection algorithm (simple behavior)" in {
      val a = new Node("A")

      val behavior = new SimpleTemporalBehavior(a,10)

      Utils.checkCycle(behavior) must equalTo(true)
    }

    "provide determinism detection algorithm (correct behavior)" in {
      val a = new Node("A")
      val b = new Node("B")
      val c = new Node("C")

      val t1 = new Transition(a,b, new TrueCondition)
      val t2 = new Transition(b,c, new TrueCondition)
      val t3 = new Transition(c,a, new TrueCondition)
      val behavior = new Behavior(a, Set[Node](a,b,c), Set[Transition](t1,t2,t3))

      Utils.checkDeterminism(behavior) must equalTo(true)
    }

    "provide determinism detection algorithm (behavior with multi transitions going from nodes)" in {
      val a = new Node("A")
      val b = new Node("B")
      val c = new Node("C")

      val t1 = new Transition(a,a, new TrueCondition)
      val t2 = new Transition(a,b, new TrueCondition)
      val t3 = new Transition(a,c, new TrueCondition)
      val t4 = new Transition(b,b, new TrueCondition)
      val t5 = new Transition(b,c, new TrueCondition)
      val t6 = new Transition(c,a, new TrueCondition)
      val behavior = new Behavior(a, Set[Node](a,b,c), Set[Transition](t1,t2,t3,t4,t5,t6))

      Utils.checkDeterminism(behavior) must equalTo(false)
    }

    "provide determinism detection algorithm (simple behavior)" in {
      val a = new Node("A")

      val behavior = new SimpleTemporalBehavior(a,10)

      Utils.checkDeterminism(behavior) must equalTo(true)
    }

    "check is a behavior is a valid one (correct behavior)" in {
      val a = new Node("A")
      val b = new Node("B")
      val c = new Node("C")

      val t1 = new Transition(a,b, new TrueCondition)
      val t2 = new Transition(b,c, new TrueCondition)
      val t3 = new Transition(c,a, new TrueCondition)
      val behavior = new Behavior(a, Set[Node](a,b,c), Set[Transition](t1,t2,t3))

      Utils.isCorrectBehavior(behavior) must equalTo(true)
    }

    "check is a behavior is a valid one (incorrect behavior)" in {
      val a = new Node("A")
      val b = new Node("B")
      val c = new Node("C")
      val d = new Node("D")

      val t1 = new Transition(a,b, new TrueCondition)
      val t2 = new Transition(b,c, new TrueCondition)
      val t3 = new Transition(c,a, new TrueCondition)
      val t4 = new Transition(a,d, new TrueCondition)
      val t5 = new Transition(a,c, new TrueCondition)
      val behavior = new Behavior(a, Set[Node](a,b,c,c), Set[Transition](t1,t2,t3,t4,t5))

      Utils.isCorrectBehavior(behavior) must equalTo(false)
    }

    "check is a behavior is a valid one (simple behavior)" in {
      val a = new Node("A")

      val behavior = new SimpleTemporalBehavior(a,10)

      Utils.isCorrectBehavior(behavior) must equalTo(true)
    }

  }

}
