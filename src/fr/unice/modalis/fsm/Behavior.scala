package fr.unice.modalis.fsm

import scala.collection.mutable.Set
import fr.unice.modalis.fsm.exceptions.NodeNotFoundException

/**
 * A user's behavior
 * @constructor create a behavior with a given entry node
 */
class Behavior(entry:Node) {

			val nodes = Set[Node]()
			val transitions = Set[Transition]()

			// FSM Entry point
			val entryPoint:Node = entry

			// FSM Current Node (init: entry point)
			var currentNode:Node = entry

			/**
			 * Add a node
			 * @param node node to add
			 * @return True if the node has been added, false otherwise
			 * 
			 */
			def addNode(node: Node):Boolean = nodes.add(node)

			/**
			 * Add a transition link
			 * @param transistion transition between two nodes
			 * @return True if the transition has been added, false otherwise
			 * 
			 */
			def addTransition(transition: Transition):Boolean = 
		{
					if(nodes.contains(transition.source) && nodes.contains(transition.destination))
						transitions.add(transition)
						else
							throw new NodeNotFoundException()
		}
			
			  
			// Add entry point to the current Node set
			addNode(entry)
			
		/**
		 * Step function (Play a deterministic automate - no condition checking. Useful for debug)
		 */
		def step:Unit=
		{ val possibleTransition = transitions.filter(x => x.source.equals(currentNode))
			  currentNode = possibleTransition.head.destination
	    }
			
}