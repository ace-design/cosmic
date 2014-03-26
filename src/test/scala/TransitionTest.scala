import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.core._
import org.specs2.mutable._

/**
 * Created by Cyril Cecchinel on 24/03/2014.
 */
class TransitionTest extends Specification {
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
  }
}
