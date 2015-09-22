import fr.unice.modalis.cosmic.core._
import fr.unice.modalis.cosmic.core.condition.TickCondition
import org.specs2.mutable.SpecificationWithJUnit

/**
 * Created by Cyril Cecchinel on 24/03/2014.
 */
class TransitionTest extends SpecificationWithJUnit {
  "A transition" should {
    "have a source" in {
      val t = new Transition(new Node("A"), new Node("B"), new TickCondition(5));
      t.source.name mustEqual "A"
    }
    "have a destination" in {
      val t = new Transition(new Node("A"), new Node("B"), new TickCondition(5));
      t.destination.name mustEqual "B"
    }
    "have a condition" in {
      val t = new Transition(new Node("A"), new Node("B"), new TickCondition(5));
      t.condition mustNotEqual null
    }

    "be equal to a same transition (1)" in {
      val n1 = new Node("A")
      val n2 = new Node("B")
      val t1 = new Transition(n1, n2, new TickCondition(5));
      val t2 = new Transition(n1, n2, new TickCondition(5));

      t1.equals(t2) mustEqual (true)
    }

    "be equal to a same transition (2)" in {
      val t1 = new Transition(new Node("A"), new Node("B"), new TickCondition(5));
      val t2 = new Transition(new Node("A"), new Node("B"), new TickCondition(5));

      t1.equals(t2) mustEqual (true)
    }

    "not be equal to a different transition (1)" in {
      val n1 = new Node("A")
      val n2 = new Node("B")
      val n3 = new Node("C")
      val t1 = new Transition(n1, n2, new TickCondition(5));
      val t2 = new Transition(n1, n3, new TickCondition(5));

      t1.equals(t2) mustEqual (false)
    }

    "not be equal to a different transition (2)" in {
      val n1 = new Node("A")
      val n2 = new Node("B")
      val n3 = new Node("C")
      val t1 = new Transition(n1, n2, new TickCondition(5));
      val t2 = new Transition(n1, n3, new TickCondition(7));

      t1.equals(t2) mustEqual (false)
    }


  }
}
