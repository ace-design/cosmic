import fr.unice.modalis.fsm.actions.{EmitStateAction, OffStateAction, StateAction}
import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.core.Node
import org.specs2.mutable.Specification
import scala.collection.mutable.Set

/**
 * Created by Cyril Cecchinel on 24/03/2014.
 */
class NodeTest extends Specification{
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
  }
}
