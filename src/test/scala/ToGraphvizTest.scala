import fr.unice.modalis.fsm.condition.{TrueCondition, TickCondition}
import fr.unice.modalis.fsm.converter.ToGraphviz
import fr.unice.modalis.fsm.core.{Behavior, Transition, Node}

import org.specs2.mutable.Specification

/**
 * Created by cyrilcecchinel on 03/04/2014.
 */
class ToGraphvizTest extends Specification{
  "A Graphviz translator" should {
    "Translate temporal transitions" in {
      val t = new Transition(new Node("A"), new Node("B"), new TickCondition(3))
      ToGraphviz.generateTransitionCode(t).replaceAll("\\s","").trim mustEqual "A -> B [ label = \"t%3==0\" ];".replaceAll("\\s","").trim;
    }
    "Translate true transitions" in {
      val t = new Transition(new Node("A"), new Node("B"), new TrueCondition)
      ToGraphviz.generateTransitionCode(t).replaceAll("\\s","").trim mustEqual "A -> B [ label = \"*\" ];".replaceAll("\\s","").trim;
    }

    "Translate node shapes" in {
      ToGraphviz.generateNodeShape("doublecircle").replaceAll("\\s","").trim mustEqual "node [shape = doublecircle];".replaceAll("\\s","").trim;
    }

    "Translate a complete behavior" in {
      val a = new Node("A")
      val b = new Node("B")
      val t = new Transition(a,b, new TickCondition(1))
      val t2 = new Transition(b,a, new TrueCondition)
      val behavior = new Behavior(a).addNode(b).addTransition(t).addTransition(t2)
      val resultConvert = ToGraphviz.generateCode(behavior).replaceAll("\\s","").replaceAll("\\n","").trim
      val expected = """digraph finite_state_machine {
        #rankdir=LR;
        #size="8,5"
        #node [shape = doublecircle]; A;
        #node [shape = circle];
        #A -> B [ label = "t%1==0" ];
        #B -> A [ label = "*" ];}""".stripMargin('#').replaceAll("\\s","").replaceAll("\\n","").trim;
      resultConvert mustEqual expected
    }
  }
}
