import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.core.{Transition, Node, Behavior}
import fr.unice.modalis.fsm.vm.{VirtualMachine, Action, AddTransition, DeleteNode}
import org.specs2.mutable.SpecificationWithJUnit
import scala.collection.mutable.ArrayBuffer

/**
 * Created by cyrilcecchinel on 31/03/2014.
 */
class VirtualMachineTest extends SpecificationWithJUnit{
  "A Virtual machine" should {
       "apply a action list on a behavior " in {
         val n1:Node = new Node("A")
         val n2:Node = new Node("B")
         val n3:Node = new Node("C")
         val b = new Behavior(n1).addNode(n2).addNode(n3)

         val actions = ArrayBuffer[Action]()
         actions += new DeleteNode(n3)
         actions += new AddTransition(new Transition(n1, n2, new TickCondition(2)))


         val t:Behavior = VirtualMachine(b, actions.toList)

         (t.nodes.size must_== 2) and (t.transitions.size must_== 1)
       }
  }
}
