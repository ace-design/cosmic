import fr.unice.modalis.fsm._
import fr.unice.modalis.fsm.actions._
import fr.unice.modalis.fsm.condition._
import fr.unice.modalis.fsm.algo.Utils

object Test extends App {
	/* This FSM represent scenario #1: Heating monitoring
	 * Sensors: Temperature sensor
	 * Frequency: 3 minutes (180s)
	 */ 
  
  val n1:Node = new Node()
  val n2:Node = new Node()
  val emit:Action = new EmitAction("datacollector", 8080)
  n2.addAction(emit)
  
  
  val t1:Transition = new Transition(n1, n2, new TickCondition(180))
  val t2:Transition = new Transition(n2 ,n1, new TrueCondition)
  
  val heatingRegulation:Behavior = new Behavior(n1)
  heatingRegulation.addNode(n2)
  heatingRegulation.addTransition(t1)
  heatingRegulation.addTransition(t2)

  // Heating regulation FSM done
  heatingRegulation.step
  heatingRegulation.step
 
 // Utils.develop(heatingRegulation)
}