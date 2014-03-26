import fr.unice.modalis.fsm.core._

import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.condition.TrueCondition
import fr.unice.modalis.fsm.algo.FSMAlgo

object TestFSM extends App {

  val n1:Node = new Node("A", null)
  val n2:Node = new Node("B", null)
  val n3:Node = new Node("C", null)

  
  val t1:Transition = new Transition(n1,n2, new TickCondition(3))
  val t2:Transition = new Transition(n2, n3, new TickCondition(7))
  val t3:Transition = new Transition(n3, n1, new TrueCondition)
  
  val fsm:Behavior = new Behavior(n1, Set[Node](n1,n2,n3), Set[Transition](t1,t2,t3))

  println(FSMAlgo.develop(t1))

  
}