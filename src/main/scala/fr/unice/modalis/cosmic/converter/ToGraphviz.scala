package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.actions.guard.GuardAction
import fr.unice.modalis.cosmic.core.condition.TickCondition
import fr.unice.modalis.cosmic.core.{Behavior, Node, Transition}

/**
 * Graphviz translator
 */
object ToGraphviz {

  def apply(b: Behavior): String = generateCode(b)

  /**
   * Generate a Graphviz script from a behavior
   * @param b Behavior
   * @return A graphviz script
   */
  def generateCode(b: Behavior): String = {
    val s = new StringBuilder
    s.append(generateHeader)
    // Generate entry point
    s.append(generateNodeShape("doublecircle"))
    s.append(generateNodeCode(b.entryPoint) + ";\n")
    // Generate states
    s.append(generateNodeShape("circle"))
    b.nodes.filterNot(n => n.equals(b.entryPoint)).foreach(n => s.append(generateNodeCode(n) + ";\n"))
    b.transitions.foreach(t => s.append(generateTransitionCode(t)))

    // Generate constraint
    s.append(generateNodeShape("record"))

    b.nodes.filter(n => n.constraintsAmount() > 0).foreach(n => s.append(generateConstraints(n)))

    s.append(generateFooter())
    s.toString()
  }


  def generateNodeShape(shape: String) = "node [shape = " + shape + "]; \n"

  def generateHeader() = {
    "digraph finite_state_machine { rankdir=LR; size=\"13\"\n"
  }

  def generateTransitionCode(t: Transition) = {
    val tname = t.condition match {
      case TickCondition(n) => n + "s"
      case _ => t.condition.toString

    }
    t.source.name + "->" + t.destination.name + " [ label = \"" + tname + "\"];\n"
  }

  def generateNodeCode(n: Node) = {
    n.name + "[label=\"" + (if (n.actions.size > 0) n.actions else "IDLE") + "\"]"
  }

  def generateConstraints(n: Node): String = {
    val s = new StringBuilder
    var i = 1

    for (f <- n.actions.actions.toIterator) {

      if (f.guards.size > 0) {
        val box = n.name + "_CONSTRAINTS" + i
        s.append(box)
        s.append(printConstraints(f.toString(), f.guards))
        def a = box + "->" + n.name + " [dir=\"forward\",arrowhead=\"diamond\",arrowtail=\"normal\"];\n"
        s.append(a)
        i += 1
      }

    }


    s.toString()

  }

  def printConstraints(a: String, l: List[GuardAction]): String = {
    def printInternal(i: Int, c: List[GuardAction]): String = {
      c match {
        case Nil => ""
        case c :: l => "|<f" + i + ">" + c.toString + "\\n" + printInternal(i + 1, l)
      }
    }
    "[label=\"{<f0>" + a + printInternal(1, l) + "}\"];"

  }

  def generateFooter() = "}\n"
}
