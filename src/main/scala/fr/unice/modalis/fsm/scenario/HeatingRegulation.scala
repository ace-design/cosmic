package fr.unice.modalis.fsm.scenario

import fr.unice.modalis.fsm.core._
import fr.unice.modalis.fsm.actions._
import fr.unice.modalis.fsm.condition._
import fr.unice.modalis.fsm.algo.Transformation
import fr.unice.modalis.fsm.vm.{Action, VirtualMachine}
import fr.unice.modalis.fsm.converter.ToGraphviz
import java.io.{PrintWriter, File}

object HeatingRegulation extends App {
	/* This FSM represents scenario #1: Heating monitoring
	 * Sensors: Temperature sensor
	 * Frequency: 3 minutes (180s)
	 */ 
		val n1:Node = new Node("Start")
		val n2:Node = new Node("Emit"); n2.addAction(new EmitStateAction("datacollector", 8080))

		val t1:Transition = new Transition(n1, n2, new TickCondition(180))
		val t2:Transition = new Transition(n2 ,n1, new TrueCondition)

		val heatingRegulation:Behavior = new Behavior(n1).addNode(n2).addTransition(t1).addTransition(t2)

    // Affiche l'automate
    println(heatingRegulation)

    // Code Graphviz
    println(ToGraphviz.generateCode(heatingRegulation))


    // Automate développé
    val actionsSet = Transformation.develop(heatingRegulation)
    val developedAutomata = VirtualMachine(heatingRegulation, actionsSet)
    val writer = new PrintWriter(new File("developedFSM1.dot"))
    writer.write(ToGraphviz.generateCode(developedAutomata))
    writer.close()



}