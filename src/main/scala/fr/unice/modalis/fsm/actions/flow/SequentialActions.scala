package fr.unice.modalis.fsm.actions.flow

import fr.unice.modalis.fsm.actions.unit.Action
import scala.collection.mutable.Seq
/**
 * Created by cyrilcecchinel on 15/04/2014.
 */
case class SequentialActions(private var actions:Seq[Action]) {

  def this() = this(Seq[Action]())

  def add(a:Action):Unit = actions = this.actions.+:(a)

  def size:Int = actions.size

  def union(s:SequentialActions):SequentialActions = new SequentialActions(s.actions.union(this.actions))

  def getActions:Seq[Action] = actions.clone()
}
