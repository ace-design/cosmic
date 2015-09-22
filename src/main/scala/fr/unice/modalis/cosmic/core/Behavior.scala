package fr.unice.modalis.cosmic.core

import fr.unice.modalis.cosmic.algo.Transformation
import fr.unice.modalis.cosmic.core.condition.TickCondition
import fr.unice.modalis.cosmic.exceptions.NodeNotFoundException

import scala.util.Random

/**
 * A user behavior
 * @constructor create a behavior with a given entry node
 * @constructor create a behavior with a given entry node, a set of nodes and a set of transitions
 */
class Behavior(entry: Node, nodesSet: Set[Node], transitionSet: Set[Transition]) {

  def this(entry: Node) = this(entry, Set[Node](entry), Set[Transition]())

  val nodes: Set[Node] = nodesSet
  val transitions: Set[Transition] = transitionSet

  val entryPoint: Node = entry // FSM Entry point

  val id:String = Random.alphanumeric.take(5).mkString //Behavior id

  /**
   * Add a node
   * @param node node to add
   * @return A new behavior with the node added
   *
   */
  def addNode(node: Node): Behavior = new Behavior(entry, nodes + node, transitions)

  /**
   * Add nodes
   * @param nodesL Nodes list
   * @return A new behavior with the nodes added
   */
  def addNodes(nodesL: List[Node]) = {
    var processedBehavior: Behavior = this
    nodesL.foreach(n => processedBehavior = processedBehavior.addNode(n))
    processedBehavior
  }

  /**
   * Delete a node
   * @param node node to be deleted
   * @return A new behavior with the node deleted
   */
  def deleteNode(node: Node): Behavior = {
    new Behavior(entry, nodes - node, transitions)
  }


  /**
   * Add a transition link
   * @param transition transition between two nodes
   * @return A new behavior with the transition added
   *
   */
  def addTransition(transition: Transition): Behavior = {
    if (nodes.filter(p => p.name == transition.source.name).size == 0)
      throw new NodeNotFoundException(transition.source)
    if (nodes.filter(p => p.name == transition.destination.name).size == 0)
      throw new NodeNotFoundException(transition.destination)

    new Behavior(entryPoint, nodes, transitions + transition)

  }

  /**
   * Add transition links
   * @param transitionsL transitions list
   * @return A new behavior with the transitions added
   */
  def addTransitions(transitionsL: List[Transition]): Behavior = {
    var processedBehavior: Behavior = this
    transitionsL.foreach(t => processedBehavior = processedBehavior.addTransition(t))
    processedBehavior
  }

  /**
   * Delete a transition link
   * @param  transition transition to be deleted
   * @return  A new behavior with the transition deleted
   */
  def deleteTransition(transition: Transition): Behavior = {
    val newSet = transitions.filterNot(p => (p.source.name == transition.source.name) && (p.destination.name == transition.destination.name) && (p.condition.equals(transition.condition)))
    new Behavior(entryPoint, nodes, newSet)
  }

  /**
   * Get the node accessed at tick t
   * @param t tick
   * @return Node accessed at tick t
   */
  def nodeAt(t: Int): Node = {
    var currentNode: Node = this.entryPoint
    var wait: Int = 0

    for (i <- 1 to t) {
      val possibleTransition: Set[Transition] = transitions.filter(x => x.source.equals(currentNode))
      possibleTransition.foreach(t => f(t))

      def f(t: Transition) = {
        t.condition match {
          case TickCondition(n) => wait += 1;
            if (wait % n == 0) {
              currentNode = t.destination;
              wait = 0
            }
          case _ => /* NOP */
        }
      }

    }
    currentNode
  }

  /**
   * Return if a new node is reached at tick t
   * @param t Tick
   * @return True if a new node is reached at tick t
   */
  def newNodeAt(t: Int): Boolean = {
    // Entry point
    if (t % period() == 0) {
      true
    }
    else {
      // Limit-case : 1 node with a single transition looping on it
      if (this.nodes.size == 1) {
        val tr: Transition = transitions.filter(t => t.source == entryPoint && t.destination == entryPoint).head
        tr.condition match {
          case TickCondition(n) if (t % n == 0) => true
          case _ => false
        }
      }
      else nodeAt(t - 1) != nodeAt(t) // Check if previous and current node are different
    }
  }

  /**
   * Get the behavior period
   * @return Behavior period
   */
  def period(): Int = {
    var currentNode: Node = this.entryPoint
    var nextNode: Node = null
    var i = 0
    while (nextNode != entryPoint) {
      val tr = transitions.filter(x => x.source.equals(currentNode) && x.condition.isInstanceOf[TickCondition]).head
      tr.condition match {
        case TickCondition(n) => i += n
      }
      nextNode = tr.destination
      currentNode = tr.destination
    }
    i
  }

  /**
   * Compose behavior
   * @param b Behavior to be composed with
   * @return Composed behavior
   */
  def +(b: Behavior): Behavior = if (b == this) this else Transformation.compose(this, b)

  def deploy() = Transformation.deploy(this)

  override def toString(): String = "FSM: Nodes=" + nodes + " Transitions=" + transitions

}