package fr.unice.modalis.fsm.scenario

import fr.unice.modalis.fsm.core._
import fr.unice.modalis.fsm.actions._
import fr.unice.modalis.fsm.condition._
import fr.unice.modalis.fsm.algo.Transformation
import fr.unice.modalis.fsm.vm.{Action, VirtualMachine}

object HeatingRegulation extends App {
	/* This FSM represent scenario #1: Heating monitoring
	 * Sensors: Temperature sensor
	 * Frequency: 3 minutes (180s)
	 */ 
		val n1:Node = new Node("Start", null)
		val n2:Node = new Node("Emit", Set[StateAction](new EmitStateAction("datacollector", 8080)))

		val t1:Transition = new Transition(n1, n2, new TickCondition(2))
		val t2:Transition = new Transition(n2 ,n1, new TrueCondition)

		val heatingRegulation:Behavior = new Behavior(n1).addNode(n2).addTransition(t1).addTransition(t2)
		println(heatingRegulation)

    // Compute actions to develop t1 transition
    val actions = Transformation.develop(t1)
}