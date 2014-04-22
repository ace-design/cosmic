package fr.unice.modalis.fsm.actions.flow

import fr.unice.modalis.fsm.actions.unit.Action
/**
 * SequentialActions
 * Represents sequential actions a1;a2; ...;an
 */
case class SequentialActions(val actions:Seq[Action]) {

  def this() = this(Seq[Action]())

  /**
   * Add an action in the Sequence
   * @param a Action
   * @return Copy of the sequence with action added at the end
   */
  def add(a:Action):SequentialActions = new SequentialActions(actions :+ a)

  /**
   * Sequence size
   * @return Number of actions
   */
  def size:Int = actions.size

  /**
   * Unify current sequence with an other sequence
   * @param s Other sequence
   * @return New unified sequence
   */
  def union(s:SequentialActions):SequentialActions = new SequentialActions(s.actions.union(this.actions))

  /**
   * For each on actions
   * @param f
   */
  def foreach(f:(Action => Unit)) = actions.foreach(f)
}
