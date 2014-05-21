import fr.unice.modalis.cosmic.actions.unit.EmitAction
import fr.unice.modalis.cosmic.core.Node
import fr.unice.modalis.cosmic.actions.guard.constraint.TimeConstraint
import org.specs2.mutable.SpecificationWithJUnit

/**
 * Created by Cyril Cecchinel on 24/03/2014.
 */
class NodeTest extends SpecificationWithJUnit {
  "Node" should {
    "tell his name" in {
      val node = new Node("A");
      node.name mustEqual "A"
    }

    "must be immmutable" in {
      val node = new Node("Emit")

      val node2 = node.addAction(new EmitAction("datacollector", 8080)).addAction(new EmitAction("a", 0))

      (node.actions.size mustEqual 0) && (node2.actions.size mustEqual 2)

    }

    "allow non action" in {
      val node = new Node("Idle")
      node.actions.actions.size mustEqual 0
    }

    "allow adding actions from a idle node" in {
      val node = new Node("Idle").addAction(new EmitAction("a", 0))
      node.actions.size mustEqual 1
    }

    "equal to an other node (same nodes)" in {
      val node = new Node("A").addAction(new EmitAction("a", 0))
      val node2 = new Node("A").addAction(new EmitAction("a", 0))

      node.equals(node2) mustEqual (true)
    }

    "equal to an other node (different nodes)" in {
      val node = new Node("A").addAction(new EmitAction("a", 0))
      val node2 = new Node("A").addAction(new EmitAction("b", 0))

      node.equals(node2) mustEqual (false)
    }

    "return the amount of constraint fixed on" in {
      val node = new Node("A").addAction(new EmitAction("a", 0)
        .addGuard(new TimeConstraint("08:00", "09:00"))
        .addGuard(new TimeConstraint("12:00", "13:00")))
        .addAction(new EmitAction("a", 0)
        .addGuard(new TimeConstraint("03:00", "04:00")))
      node.constraintsAmount() mustEqual (3)
    }
  }
}
