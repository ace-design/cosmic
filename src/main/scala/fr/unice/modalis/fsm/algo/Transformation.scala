package fr.unice.modalis.fsm.algo

import fr.unice.modalis.fsm.core._
import scala.collection.mutable.ArrayBuffer
import fr.unice.modalis.fsm.vm._
import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.exceptions.IncompatibleDevelopConditionException



/**
 * This object contains algorithms used to transform behaviors
 */
object Transformation {
		  
	def develop(t: Transition):List[Action] = {
	  
	  val currentSource: Node = t.source
	  val currentDestination: Node = t.destination
	  val actions = new ArrayBuffer[Action]
	  
	 
	 t.condition match {
	    case TickCondition(f) if f>1=> 
	      {
	        var newNode:Node = null
	        var previousNode:Node = null
	        
	        for (i <- 1 to f-1) yield {
	          newNode = new Node(currentSource.name + "D" + i)
	          def newTransition = 
	            if (i==1) new Transition(currentSource, newNode, new TickCondition(1))
	            else new Transition(previousNode, newNode, new TickCondition(1))
	          
	          actions += new AddNode(newNode)
	          actions += new AddTransition(newTransition)
	          previousNode = newNode
	        }
	        actions += new AddTransition(new Transition(newNode, currentDestination, new TickCondition(1)))
	        actions += new DeleteTransition(t)
	      
	    }
	    case _ => throw new IncompatibleDevelopConditionException("Impossible to develop " + t)
	  }
	  
	  actions.toList
	  }
	  
}