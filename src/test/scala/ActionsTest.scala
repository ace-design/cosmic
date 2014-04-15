import fr.unice.modalis.fsm.actions.flow.SequentialActions
import fr.unice.modalis.fsm.actions.unit.EmitAction
import org.specs2.mutable.SpecificationWithJUnit

/**
 * Created by cyrilcecchinel on 15/04/2014.
 */
class ActionsTest extends SpecificationWithJUnit{
  "A sequential actions object" should {
    "handle new actions (1)" in {
      val a = new SequentialActions().addAction(new EmitAction("a",0))

      a.actions.size must_== 1
    }

    "handle new actions (2)" in {
      val a = new SequentialActions().addAction(new EmitAction("a",0)).addAction(new EmitAction("b",0))

      a.actions.size must_== 2
    }
  }
}
