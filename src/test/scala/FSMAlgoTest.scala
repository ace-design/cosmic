import fr.unice.modalis.fsm.algo.FSMAlgo
import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.core.{Node, Transition}
import fr.unice.modalis.fsm.vm.{AddNode, DeleteTransition, Action}
import org.specs2.mutable._

/**
 * Created by Cyril Cecchinel on 24/03/2014.
 */
class FSMAlgoTest extends Specification{
  "A develop algorithm" should {
    "give a correct action list" in {
      val nodeA = new Node("A")
      val nodeB = new Node("B")
      val trans = new Transition(nodeA, nodeB, new TickCondition(3))

      val result = FSMAlgo.develop(trans)
      val expected = Set[Action](new DeleteTransition(trans), new AddNode("AD0"), new AddNode("AD1"))
    }
  }
}
