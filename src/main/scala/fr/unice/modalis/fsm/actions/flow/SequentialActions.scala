package fr.unice.modalis.fsm.actions.flow

import fr.unice.modalis.fsm.actions.unit.Action

/**
 * Created by cyrilcecchinel on 15/04/2014.
 */
class SequentialActions(val actions:Seq[Action]) {

  def this() = this(Seq[Action]())

  def addAction(a:Action):SequentialActions = new SequentialActions(this.actions.+:(a))

}
