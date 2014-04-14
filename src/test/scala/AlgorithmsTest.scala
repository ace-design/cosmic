import fr.unice.modalis.fsm.actions.OffStateAction
import fr.unice.modalis.fsm.algo.{Utils, Transformation}
import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.core.{Behavior, Transition, Node}
import fr.unice.modalis.fsm.vm.VirtualMachine
import org.specs2.mutable.Specification

/**
 * Created by cyrilcecchinel on 14/04/2014.
 */
class AlgorithmsTest extends Specification{
  "A development should maintain behavior properties (correctness and period)" in {
    val n1:Node = new Node("A");
    val n2:Node = new Node("B");
    val n3:Node = new Node("C");
    n3.addAction(new OffStateAction())

    val t1:Transition = new Transition(n1,n2,new TickCondition(3))
    val t2:Transition = new Transition(n2,n3,new TickCondition(2))
    val t3:Transition = new Transition(n3,n1,new TickCondition(1))

    val b:Behavior = new Behavior(n1).addNodes(List[Node](n2,n3)).addTransitions(List[Transition](t1,t2,t3))

    val dv:Behavior = VirtualMachine.apply(b,Transformation.develop(b))

    (Utils.isCorrectBehavior(dv) mustEqual(true)) and (b.period() must_== dv.period())
  }

  "A factorization should maintain behavior properties (correctness and period)" in {
    val n1:Node = new Node("A");
    val n2:Node = new Node("B");
    val n3:Node = new Node("C");
    n3.addAction(new OffStateAction())

    val t1:Transition = new Transition(n1,n2,new TickCondition(1))
    val t2:Transition = new Transition(n2,n3,new TickCondition(1))
    val t3:Transition = new Transition(n3,n1,new TickCondition(1))

    val b:Behavior = new Behavior(n1).addNodes(List[Node](n2,n3)).addTransitions(List[Transition](t1,t2,t3))

    val fb:Behavior = VirtualMachine.apply(b,Transformation.factorize(b))

    (Utils.isCorrectBehavior(fb) mustEqual(true)) and (b.period() must_== fb.period())
  }
}
