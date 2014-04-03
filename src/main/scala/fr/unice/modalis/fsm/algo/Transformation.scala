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


  /**
   * Develop a behavior
   * @param b Behavior
   * @return An actions list to develop the behavior
   */
	def developBehavior(b: Behavior):List[Action] = {
    val setActions = ArrayBuffer[Action]()
    b.transitions.foreach(t => setActions ++= develop(t))
    setActions.toList
  }

  /**
   * Develop a transition
   * @param t Transition
   * @return An actions list to develop the transition
   */
	def develop(t: Transition):List[Action] = {
	  
	  val currentSource: Node = t.source
	  val currentDestination: Node = t.destination
	  val actions = new ArrayBuffer[Action]
	  
	 
	 t.condition match {
	    case TickCondition(f) if f>1=>  // Tick condition (freq : t)
	      {
	        var newNode:Node = null
	        var previousNode:Node = null
	        
	        for (i <- 1 to f-1) yield { // Generate t-1 nodes
	          newNode = new Node(currentSource.name + "D" + i)
	          def newTransition =   // Generate transitions between newly created nodes
	            if (i==1) new Transition(currentSource, newNode, new TickCondition(1))  // First node case
	            else new Transition(previousNode, newNode, new TickCondition(1))
	          
	          actions += new AddNode(newNode)
	          actions += new AddTransition(newTransition)
	          previousNode = newNode
	        }
	        actions += new AddTransition(new Transition(newNode, currentDestination, new TickCondition(1))) // Last node case
	        actions += new DeleteTransition(t)  // Delete non-developed transition
	      
	    }
	    case _ => /* DO NOTHING */
	  }
	  
	  actions.toList
	  }
	  
}