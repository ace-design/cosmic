import fr.unice.modalis.fsm.Node
import fr.unice.modalis.fsm.Transition
import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.condition.TrueCondition
import fr.unice.modalis.fsm.Behavior
import fr.unice.modalis.fsm.algo.Utils

object TestFSM extends App {

  val n1,n2,n3:Node = new Node
  
  val t1:Transition = new Transition(n1,n2, new TickCondition(3))
  val t2:Transition = new Transition(n2, n3, new TickCondition(7))
  val t3:Transition = new Transition(n3, n1, new TrueCondition)
  
  val fsm:Behavior = new Behavior(n1)
  
  fsm.addNode(n2)
  fsm.addNode(n3)
  
  fsm.addTransition(t1)
  fsm.addTransition(t2)
  fsm.addTransition(t3)
  
}