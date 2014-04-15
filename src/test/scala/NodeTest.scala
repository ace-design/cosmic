import fr.unice.modalis.fsm.actions.constraints.TimeConstraint
import fr.unice.modalis.fsm.actions.{EmitStateAction, OffStateAction, StateAction}
import fr.unice.modalis.fsm.core.Node
import org.specs2.mutable.SpecificationWithJUnit
import scala.collection.mutable.Set

/**
 * Created by Cyril Cecchinel on 24/03/2014.
 */
class NodeTest extends SpecificationWithJUnit{
  "Node" should {
    "tell his name" in {
      val node = new Node("A");
      node.name mustEqual "A"
    }

    "must be mutable" in {
      val node = new Node("Emit", Set[StateAction](new EmitStateAction("datacollector", 8080)))
      val action:StateAction = new OffStateAction
      node.addAction(action)

      node.actions.size mustEqual 2

    }

    "allow non action" in {
      val node = new Node("Idle")
      node.actions.size mustEqual 0
    }

    "allow adding actions from a idle node" in {
      val node = new Node("Idle")
      node.addAction(new OffStateAction)
      node.actions.size mustEqual 1
    }

    "equal to an other node (same nodes)" in {
      val node = new Node("A")
      val node2 = new Node("A")

      node.addAction(new EmitStateAction("a",0))
      node2.addAction(new EmitStateAction("a",0))

      node.equals(node2) mustEqual(true)
    }

    "equal to an other node (different nodes)" in {
      val node = new Node("A")
      val node2 = new Node("A")

      node.addAction(new EmitStateAction("a",0))
      node2.addAction(new EmitStateAction("b",0))

      node.equals(node2) mustEqual(false)
    }

    "return the amount of constraints fixed on" in {
      val node = new Node("A")
      node.addAction(new EmitStateAction("a",0).addConstrain(new TimeConstraint("08:00", "09:00")).addConstrain(new TimeConstraint("12:00", "13:00")))
      node.addAction(new OffStateAction().addConstrain(new TimeConstraint("03:00", "04:00")))
      node.constraintsAmount() mustEqual(3)
    }
  }
}
