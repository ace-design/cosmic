import fr.unice.modalis.fsm.condition._
import org.specs2.mutable.SpecificationWithJUnit

/**
 * Created by cyrilcecchinel on 14/04/2014.
 */
class ConditionTest extends SpecificationWithJUnit {
  "A condition" should {
    "be equals to an other condition (true case)" in {
      val t1: Condition = new TickCondition(2)
      val t2: Condition = new TimeCondition("08:00")
      val t3: Condition = new TickCondition(1)

      val t1a: Condition = new TickCondition(2)
      val t2a: Condition = new TimeCondition("08:00")
      val t3a: Condition = new TickCondition(1)

      (t1.equals(t1a) mustEqual (true)) && (t2.equals(t2a) mustEqual (true)) && (t3.equals(t3a) mustEqual (true))
    }

    "be equals to an other condition (false case)" in {
      val t2: Condition = new TimeCondition("08:00")

      val t3a: Condition = new TickCondition(1)

      t2.equals(t3a) mustEqual (false)
    }
  }
}
