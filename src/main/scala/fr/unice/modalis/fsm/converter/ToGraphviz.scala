package fr.unice.modalis.fsm.converter

import fr.unice.modalis.fsm.core.{Node, Transition, Behavior}
import fr.unice.modalis.fsm.condition.{TrueCondition, TickCondition}

/**
 * Graphviz translator
 */
object ToGraphviz {

  /**
   * Generate a Graphviz script from a behavior
   * @param b Behavior
   * @return A graphviz script
   */
  def convert(b: Behavior):String = {
    val s = new StringBuilder
    s.append(generateHeader)
    s.append(generateNode("doublecircle"))
    s.append(generateNodeCode(b.entryPoint) + "\n")
    s.append(generateNode("circle"))
    b.transitions.foreach(t => s.append(generateTransitionCode(t)))
    s.append(generateFooter)

    s.toString()
  }

  private def generateNode(shape:String) = "node [shape = " + shape + "]; \n"

  private def generateHeader()=
  {
    "digraph finite_state_machine { rankdir=LR; size=\"8,5\"\n"
  }
  private def generateTransitionCode(t: Transition) =
  {
    val tname = t.condition match {
      case TickCondition(n) => "t%" + n +"==0"
      case TrueCondition() => "*"
      case _ => t.condition.toString

    }
    t.source.name + "->" + t.destination.name + " [ label = \"" + tname + "\"];\n"}

  private def generateNodeCode(n: Node) =
  { n.name }

  private def generateFooter() = "}\n"

}
