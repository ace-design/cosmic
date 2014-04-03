package fr.unice.modalis.fsm.converter

import fr.unice.modalis.fsm.core.{Node, Transition, Behavior}
import fr.unice.modalis.fsm.condition.{TrueCondition, TickCondition}
import scala.sys.process._

/**
 * Graphviz translator
 */
object ToGraphviz {

  /**
   * Generate a Graphviz script from a behavior
   * @param b Behavior
   * @return A graphviz script
   */
  def generateCode(b: Behavior):String = {
    val s = new StringBuilder
    s.append(generateHeader)
    s.append(generateNodeShape("doublecircle"))
    s.append(generateNodeCode(b.entryPoint) + ";\n")
    s.append(generateNodeShape("circle"))
    b.transitions.foreach(t => s.append(generateTransitionCode(t)))
    s.append(generateFooter)

    s.toString()
  }

  def generateNodeShape(shape:String) = "node [shape = " + shape + "]; \n"

  def generateHeader()=
  {
    "digraph finite_state_machine { rankdir=LR; size=\"8,5\"\n"
  }
  def generateTransitionCode(t: Transition) =
  {
    val tname = t.condition match {
      case TickCondition(n) => "t%" + n +"==0"
      case TrueCondition() => "*"
      case _ => t.condition.toString

    }
    t.source.name + "->" + t.destination.name + " [ label = \"" + tname + "\"];\n"}

  def generateNodeCode(n: Node) =
  { n.name }

  def generateFooter() = "}\n"
}
