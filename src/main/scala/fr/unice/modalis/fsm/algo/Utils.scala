package fr.unice.modalis.fsm.algo

import fr.unice.modalis.fsm.core.{Transition, Node, SimpleTemporalBehavior, Behavior}
import fr.unice.modalis.fsm.vm.VirtualMachine
import fr.unice.modalis.fsm.condition.TickCondition

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
  def gcd(a: Int, b: Int):Int=if (b==0) a.abs else gcd(b, a%b)

  /**
   * Compute the lcd
   * @param a a
   * @param b b
   * @return The least commin divisor between a and b
   */
  def lcm(a: Int, b: Int)=(a*b).abs/gcd(a,b)

  /**
   * Compute lcd over a number list
   * @param args Int list
   * @return The greatest common divisor between each number
   */
  def lcmm(args:List[Int]):Int =
    args match {
      case a::b::Nil => lcm(a,b)
      case a::b::tail => lcmm(lcm(a,b)::tail)
    }

  /**
   * Compute a new developed temporal automata
   * @param per Period
   * @return An automata with per+1 states and per transitions
   */
  def generateDevelopedTemporalBlankAutomata(per:Int):Behavior = {
    val node = new Node("Gen");
    val b = new Behavior(node).addTransition(new Transition(node,node,new TickCondition(per)))
    VirtualMachine.apply(b, Transformation.develop(b))
  }

  /**
   * Check if a behavior is a valid one
   * Condition #1: Deterministic automata
   * Condition #2: Cyclic automata
   * @param b User behavior
   * @return Boolean corresponding to the validation
   */
  def checkBehavior(b:Behavior):Boolean = {
    // Todo
    true
  }
}
