package fr.unice.modalis.fsm.scenario

import fr.unice.modalis.fsm.core.{Behavior, Transition, Node}
import fr.unice.modalis.fsm.actions.EmitAction
import fr.unice.modalis.fsm.condition.TickCondition
import fr.unice.modalis.fsm.actions.constraints.TimeConstraint

/**
 * Scenario basis
 */
trait Scenario {

  def init():Behavior

}

/**
 * This represents scenario #1: Heating monitoring
 * Period: 30 seconds
 */
object HeatingMonitoring extends Scenario{

 override def init():Behavior = {
    val n = new Node("TEMP")
    n.addAction(new EmitAction("AlicePC",8080))
    val t = new Transition(n,n, new TickCondition(30))
    new Behavior(n).addTransition(t)
  }
}

/**
 * This represents scenario #2: Intrusion prevention
 * Period : 10 seconds
 */
object IntrusionPrevention extends Scenario {

  override def init(): Behavior = {
    val n = new Node("WINDOW")
    n.addAction(new EmitAction("BobPC", 8080).addConstrain(new TimeConstraint("19:00", "06:00")))
    val t = new Transition(n, n, new TickCondition(10))
    new Behavior(n).addTransition(t)
  }

}

/**
 * This represents scenario #3: Air Quality monitoring
 * Period : 45 seconds
 */
object AirQuality extends Scenario {

  override def init(): Behavior = {
    val n = new Node("ENVIRONMENT")
    n.addAction(new EmitAction("CharliePC", 8080).addConstrain(new TimeConstraint("19:00", "06:00")))
    val t = new Transition(n, n, new TickCondition(3))
    new Behavior(n).addTransition(t)
  }

}

/**
 * This represents scenario #4: Energy Loss
 * Period : 60 seconds
 */
object EnergyLoss extends Scenario {

  override def init(): Behavior = {
    val n = new Node("ENERGY")
    n.addAction(new EmitAction("DelphinePC", 8080).addConstrain(new TimeConstraint("09:00", "17:00")))
    val t = new Transition(n, n, new TickCondition(60))
    new Behavior(n).addTransition(t)
  }

}

/**
 * This represents scenario #5: Carpooling
 * Period : 120 seconds
 */
object CarPooling extends Scenario {

  override def init(): Behavior = {
    val n = new Node("PARKING")
    n.addAction(new EmitAction("Etienne", 8080).addConstrain(new TimeConstraint("08:00", "09:00")).addConstrain(new TimeConstraint("17:00", "18:00")))
    val t = new Transition(n, n, new TickCondition(2))
    new Behavior(n).addTransition(t)
  }

}

/**
 * This represents scenario #6: Window opening detection
 * Period : 10 seconds
 */
object WindowOpening extends Scenario {

  override def init(): Behavior = {
    val n = new Node("OPENING")
    n.addAction(new EmitAction("Francois", 8080).addConstrain(new TimeConstraint("09:00", "14:00")))
    val t = new Transition(n, n, new TickCondition(60))
    new Behavior(n).addTransition(t)
  }

}