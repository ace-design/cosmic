package fr.unice.modalis.cosmic.algo

import fr.unice.modalis.cosmic.core.{Transition, Node, Behavior}
import fr.unice.modalis.cosmic.algo.vm.{Instruction, AddTransition, AddNode, VirtualMachine}
import fr.unice.modalis.cosmic.core.condition.TickCondition
import fr.unice.modalis.cosmic.actions.flow.SequentialActions
import scala.collection.mutable.ArrayBuffer
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * Utils methods
 */
object Utils {

  /**
   * Compute the gcd
   * @param a a
   * @param b b
   * @return The greatest common divisor between a and b
   */
  def gcd(a: Int, b: Int): Int = {
    if (b == 0) a.abs else gcd(b, a % b)
  }

  /**
   * Compute the lcd
   * @param a a
   * @param b b
   * @return The least commin divisor between a and b
   */
  def lcm(a: Int, b: Int) = {
    (a * b).abs / gcd(a, b)
  }

  /**
   * Compute lcd over a number list
   * @param args Int list
   * @return The greatest common divisor between each number
   */
  def lcmm(args: List[Int]): Int =
    args match {
      case Nil => throw new Exception("The lcd computation needs two operands")
      case a :: Nil => throw new Exception("The lcd computation needs two operands")
      case a :: b :: Nil => lcm(a, b)
      case a :: b :: tail => lcmm(lcm(a, b) :: tail)
    }

  /**
   * Compute a new developed temporal automata
   * @param per Period
   * @return An automata with a per-Period
   */
  def generateDevelopedTemporalBlankAutomata(per: Int): Behavior = {
    val node = new Node("Gen")
    val b = new Behavior(node).addTransition(new Transition(node, node, new TickCondition(per)))
    VirtualMachine.apply(b, Transformation.develop(b))
  }

  /**
   * Compute a new developed temporal automata with a same action distributed on each state
   * @param per Period
   * @param actions Actions to distribute
   * @return An automata with a per-Period and Actions distributed on each state
   */
  def generateDevelopedTemporalRepeatedAutomata(per: Int, actions: SequentialActions): Behavior = {

    val setActions = ArrayBuffer[Instruction]()

    // Build new entry point
    val entry = new Node("Gen", actions)

    // Init : Previous node at start = entry point
    var previousNode: Node = entry

    setActions += new AddNode(entry)
    for (i <- 1 to per - 1) {

      val newNode = new Node("G" + i, actions)

      setActions += new AddNode(newNode)
      setActions += new AddTransition(new Transition(previousNode, newNode, new TickCondition(1)))

      // Update previous node with newly created one
      previousNode = newNode

    }
    setActions += new AddTransition(new Transition(previousNode, entry, new TickCondition(1)))

    VirtualMachine.apply(new Behavior(entry), setActions.toList)
  }

  /**
   * Check if a behavior is a cyclic one
   * @param b User behavior
   * @return True if the behavior is cyclic
   */
  def checkCycle(b: Behavior): Boolean = {
    def x(e: Node, t: Transition, ns: Set[Node], ts: Set[Transition]): Boolean = {
      t match {
        case Transition(a, b, _) if (b.equals(e)) => true // Entry point is next hop
        case Transition(a, b, _) if (a.equals(b)) => true // Loop-transition on a node
        case Transition(a, b, _) => {
          // find candidates transitions with source == t.destination
          val candidates = ts.filter(p => p.source.equals(b))
          var result = true
          if (candidates.size > 0) {
            candidates.foreach(c => result = result && x(e, c, ns, ts)) // Recurse on next transition
            result
          } else {
            false // No more transition are found and entry point can't be reached
          }
        }
      }
    }
    val filter = b.transitions.filter(t => t.source == b.entryPoint)
    var result = true
    filter.foreach(t => result = result && x(b.entryPoint, t, b.nodes, b.transitions))
    result
  }

  /**
   * Check if a behavior is a deterministic one
   * @param b User behavior
   * @return True if the behavior is deterministic
   */
  def checkDeterminism(b: Behavior): Boolean = {
    var result = true
    b.nodes.foreach(n => {
      val transitions = b.transitions.filter(t => t.source.equals(n))
      result = result && transitions.size == 1
    }) //Check if each node has ONLY one transition going from.
    result
  }

  /**
   * Check if a behavior is a valid one
   * Condition #1: Deterministic automata
   * Condition #2: Cyclic automata
   * @param b User behavior
   * @return Boolean corresponding to the validation
   */
  def isCorrectBehavior(b: Behavior): Boolean =
    try {
      checkCycle(b) && checkDeterminism(b)
    } catch {
      case e: StackOverflowError => false
    }

  /**
   * Parse a string time (HH:mm) into a DateTime
   * @param time Time
   * @return A DateTime object with the given time set
   */
  def parseTime(time:String):DateTime = {
    val formatter = DateTimeFormat.forPattern("HH:mm")
    formatter.parseDateTime(time)
  }

  /**
   * Combine a set elements
   * @param set Set of elements
   * @tparam A Type of elements in the set
   * @return Set of combinations
   */
  def combine[A](set: Set[A]):Set[Set[A]] = {
    def internal(s: Set[A], depth: Int): Set[Set[A]] = depth match {
      case 0 => Set(Set())
      case 1 => s map { Set(_) }
      case n => {
        val others = internal(s,n-1)
        val these = for( e <- s; o <- others ) yield o+e
        these ++ others
      }
    }
    internal(set, set.size)
  }

}
