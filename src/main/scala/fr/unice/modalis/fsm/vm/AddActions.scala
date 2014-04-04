package fr.unice.modalis.fsm.vm

import fr.unice.modalis.fsm.core.{Behavior, Node}
import fr.unice.modalis.fsm.actions.StateAction
import scala.collection.mutable.Set
/**
 * Created by cyrilcecchinel on 04/04/2014.
 */
class AddActions(n:Node, actions:Set[StateAction]) extends Action{
  override def make(b: Behavior):Behavior =
  {
    val processedBehavior:Behavior = b
    actions.foreach(a => processedBehavior.addAction(n, a))
    processedBehavior

  }

  override def toString():String = "{AddAction node=" + n + " actions=" + actions + "}"

}
