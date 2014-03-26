import fr.unice.modalis.fsm.condition.TrueCondition
import fr.unice.modalis.fsm.core.{Transition, Behavior, Node}
import fr.unice.modalis.fsm.exceptions.NodeNotFoundException
import org.specs2.mutable.Specification

/**
 * Created by Cyril Cecchinel on 26/03/2014.
 */
class BehaviorTest extends Specification {
  "Behavior" should {
    "have an entry point" in {
      val node = new Node("START")
      val behavior = new Behavior(node)
      behavior.entryPoint mustEqual node
    }

    "should be immutable" in {
      val node = new Node("START")
      val behavior = new Behavior(node)
      behavior.addNode(new Node("TEST"))
      behavior.nodes.size mustEqual 1
    }

    "not allow transitions with a not referenced node" in {
      val node = new Node("START")
      val test = new Node("TEST")
      val behavior = new Behavior(node)
      behavior.addTransition(new Transition(node, test, new TrueCondition)) must throwA[NodeNotFoundException]
    }

    "allow transition between referenced nodes" in {
      val node = new Node("START")
      val test = new Node("TEST")
      val behavior = new Behavior(node)
      behavior.addNode(test).addTransition(new Transition(node, test, new TrueCondition)) must not(throwA[NodeNotFoundException])
    }
  }
}
