import fr.unice.modalis.fsm.actions.OffStateAction
import fr.unice.modalis.fsm.converter.ToGraphviz
import fr.unice.modalis.fsm.core._

import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.condition.TrueCondition
import fr.unice.modalis.fsm.algo.Transformation
import fr.unice.modalis.fsm.vm.VirtualMachine

object TestFSM extends App {

  val n1:Node = new Node("A")
  val n2:Node = new Node("B").addAction(new OffStateAction)
  val n3:Node = new Node("C").addAction(new OffStateAction)

  
  val t1:Transition = new Transition(n1,n2, new TickCondition(2))
  val t2:Transition = new Transition(n2, n3, new TrueCondition)
  val t3:Transition = new Transition(n3, n1, new TrueCondition)
  
  val fsm:Behavior = new Behavior(n1, Set[Node](n1,n2,n3), Set[Transition](t1,t2,t3))
  println(ToGraphviz.generateCode(fsm))

  val developActions = Transformation.develop(fsm)
  val developedFsm = VirtualMachine.apply(fsm, developActions)

  println(ToGraphviz.generateCode(developedFsm))

  val factorizeActions = Transformation.factorize(developedFsm)
  println(factorizeActions)
  val factorizedFsm = VirtualMachine.apply(developedFsm, factorizeActions)

  println(ToGraphviz.generateCode(factorizedFsm))
  
}