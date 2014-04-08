package fr.unice.modalis.fsm.converter

import fr.unice.modalis.fsm.core.{Node, Transition, Behavior}
import fr.unice.modalis.fsm.condition.{TrueCondition, TickCondition}
import fr.unice.modalis.fsm.actions.StateAction

/**
 * Graphviz translator
 */
object ToGraphviz extends Converter{

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
    b.nodes.filterNot(n => n.equals(b.entryPoint)).foreach(n => s.append(generateNodeCode(n) + ";\n"))
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
  { n.name + "[label=\"" + (if (n.actions.size> 0) n.actions  else "IDLE") + "\"]"}

  def generateFooter() = "}\n"
}
