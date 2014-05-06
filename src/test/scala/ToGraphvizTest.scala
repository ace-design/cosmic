import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.converter.ToGraphviz
import fr.unice.modalis.fsm.core.{Transition, Node}

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

    "Translate node shapes" in {
      ToGraphviz.generateNodeShape("doublecircle").replaceAll("\\s","").trim mustEqual "node [shape = doublecircle];".replaceAll("\\s","").trim;
    }

  }
}
