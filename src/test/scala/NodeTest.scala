import fr.unice.modalis.fsm.actions.{EmitStateAction, OffStateAction, StateAction}
import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.core.Node
import org.specs2.mutable.Specification

/**
 * Created by Cyril Cecchinel on 24/03/2014.
 */
class NodeTest extends Specification{
  "Node" should {
    "tell his name" in {
      val node = new Node("A");
      node.name mustEqual "A"
    }

    "must be immutable" in {
      val node = new Node("Emit", Set[StateAction](new EmitStateAction("datacollector", 8080)))
      val action:StateAction = new OffStateAction
      val newNode = node.addAction(action)

      node.actions + action mustEqual newNode.actions

    }

    "allow non action" in {
      val node = new Node("Idle")
      node.actions.size mustEqual 0
    }

    "allow adding actions from a idle node" in {
      val node = new Node("Idle")
      val newNode = node.addAction(new OffStateAction)
      newNode.actions.size mustEqual 1
    }
  }
}
