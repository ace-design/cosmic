import fr.unice.modalis.fsm.core.{SimpleTemporalBehavior, Node}
import org.specs2.mutable.Specification

/**
 * Created by cyrilcecchinel on 04/04/2014.
 */
class SimpleTemporalBehaviorTest extends Specification{

  "A temportal behavior" should {
    "access the right node at time t" in {
      val node = new Node("START")
      val test = new Node("TEST")
      val behavior = new SimpleTemporalBehavior(node, test, 2)

      (behavior.nodeAt(1) mustEqual node) and (behavior.nodeAt(2) mustEqual test) and (behavior.nodeAt(5) mustEqual node)
    }
  }
}
