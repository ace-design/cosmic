import fr.unice.modalis.fsm.actions.unit.EmitAction
import fr.unice.modalis.fsm.algo.Transformation
import fr.unice.modalis.fsm.condition.{TimeCondition, TickCondition}
import fr.unice.modalis.fsm.core.{Transition, Behavior, Node}
import fr.unice.modalis.fsm.exceptions.NodeNotFoundException
import fr.unice.modalis.fsm.vm.VirtualMachine
import org.specs2.mutable.SpecificationWithJUnit

/**
 * Created by Cyril Cecchinel on 26/03/2014.
 */
class BehaviorTest extends SpecificationWithJUnit {
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
      behavior.addTransition(new Transition(node, test, new TickCondition(1))) must throwA[NodeNotFoundException]
    }

    "allow transition between referenced nodes" in {
      val node = new Node("START")
      val test = new Node("TEST")
      val behavior = new Behavior(node)
      behavior.addNode(test).addTransition(new Transition(node, test, new TickCondition(1))) must not(throwA[NodeNotFoundException])
    }

    "give correct node for a defined tick value" in {
      val n1: Node = new Node("B").addAction(new EmitAction("host", 9090))
      val n2: Node = new Node("Bnull1")
      val n3: Node = new Node("Bnull2")

      val t1: Transition = new Transition(n1, n2, new TickCondition(1))
      val t2: Transition = new Transition(n2, n3, new TickCondition(1))
      val t3: Transition = new Transition(n3, n1, new TickCondition(1))

      val behavior: Behavior = new Behavior(n1).addNodes(List[Node](n1, n2, n3)).addTransitions(List[Transition](t1, t2, t3))

      (behavior.nodeAt(0) must_== n1) and (behavior.nodeAt(2) must_== n3) and (behavior.nodeAt(4) must_== n2)
    }

    "give a correct period on simple behaviors" in {
      val n1: Node = new Node("B").addAction(new EmitAction("host", 9090))
      val n2: Node = new Node("Bnull1")
      val n3: Node = new Node("Bnull2")

      val t1: Transition = new Transition(n1, n2, new TickCondition(1))
      val t2: Transition = new Transition(n2, n3, new TickCondition(1))
      val t3: Transition = new Transition(n3, n1, new TickCondition(1))

      val behavior: Behavior = new Behavior(n1).addNodes(List[Node](n1, n2, n3)).addTransitions(List[Transition](t1, t2, t3))

      behavior.period() must_== 3
    }

    "give a correct period on complex behaviors" in {
      val n1: Node = new Node("B").addAction(new EmitAction("host", 9090))
      val n2: Node = new Node("Bnull1")
      val n3: Node = new Node("Bnull2")
      val n4: Node = new Node("Off")

      val t1: Transition = new Transition(n1, n2, new TickCondition(1))
      val t2: Transition = new Transition(n2, n3, new TickCondition(1))
      val t3: Transition = new Transition(n3, n1, new TickCondition(1))
      val t4: Transition = new Transition(n1, n4, new TimeCondition("08:00"))
      val t5: Transition = new Transition(n4, n1, new TimeCondition("09:00"))
      val t6: Transition = new Transition(n2, n1, new TickCondition(1))

      val behavior: Behavior = new Behavior(n1).addNodes(List[Node](n1, n2, n3, n4)).addTransitions(List[Transition](t1, t2, t3, t4, t5))

      behavior.period() must_== 3
    }


    "tell if a new node is accessed at a defined tick (a transition is crossed at tick t) (1)" in {
      val n1: Node = new Node("A")
      val n2: Node = new Node("B")
      val n3: Node = new Node("C")

      val t1: Transition = new Transition(n1, n2, new TickCondition(1))
      val t2: Transition = new Transition(n2, n3, new TickCondition(2))
      val t3: Transition = new Transition(n3, n1, new TickCondition(3))

      val behavior: Behavior = new Behavior(n1).addNodes(List[Node](n1, n2, n3)).addTransitions(List[Transition](t1, t2, t3))

      (behavior.newNodeAt(1) mustEqual (true)) && (behavior.newNodeAt(3) mustEqual (true)) && (behavior.newNodeAt(7) mustEqual (true)) && (behavior.newNodeAt(8) mustEqual (false))
    }

    "tell if a new node is accessed at a defined tick (a transition is crossed at tick t) (2)" in {
      val n1: Node = new Node("A")
      val t1: Transition = new Transition(n1, n1, new TickCondition(4))

      val behavior: Behavior = new Behavior(n1).addNode(n1).addTransition(t1)

      (behavior.newNodeAt(1) mustEqual (false)) && (behavior.newNodeAt(4) mustEqual (true)) && (behavior.newNodeAt(0) mustEqual (true))
    }

    "entry node must be considered as a new accessed node" in {
      val n1: Node = new Node("A")
      val n2: Node = new Node("B")
      val n3: Node = new Node("C")

      val t1: Transition = new Transition(n1, n2, new TickCondition(1))
      val t2: Transition = new Transition(n2, n3, new TickCondition(2))
      val t3: Transition = new Transition(n3, n1, new TickCondition(3))

      val behavior: Behavior = new Behavior(n1).addNodes(List[Node](n1, n2, n3)).addTransitions(List[Transition](t1, t2, t3))

      behavior.newNodeAt(0) mustEqual true
    }


    "new accessed node on complex behaviors" in {
      val n1: Node = new Node("A").addAction(new EmitAction("host", 9090))
      val n2: Node = new Node("B")
      val n3: Node = new Node("C")
      val n4: Node = new Node("D").addAction(new EmitAction("a", 0))

      val t1: Transition = new Transition(n1, n2, new TickCondition(2))
      val t2: Transition = new Transition(n2, n3, new TickCondition(1))
      val t3: Transition = new Transition(n3, n4, new TickCondition(3))
      val t4: Transition = new Transition(n4, n1, new TickCondition(1))

      val behavior: Behavior = new Behavior(n1).addNodes(List[Node](n1, n2, n3, n4)).addTransitions(List[Transition](t1, t2, t3, t4))

      val fact = VirtualMachine(behavior, Transformation.factorize(behavior))
      (fact.newNodeAt(0) mustEqual (true)) && (fact.newNodeAt(6) mustEqual (true)) && (fact.newNodeAt(7) mustEqual (true))


    }
  }
}
