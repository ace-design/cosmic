package fr.unice.modalis.fsm.actions.flow

import fr.unice.modalis.fsm.actions.unit.Action
import scala.util.Random

/**
 * Action flow
 */
trait ActionFlow{

  val flowId = Random.alphanumeric.take(5).mkString

  val actions: Iterable[Action]

  def add(a: Action): ActionFlow


  def size: Int

  def union(s: SequentialActions): ActionFlow

  def foreach(f: (Action => Unit))
}