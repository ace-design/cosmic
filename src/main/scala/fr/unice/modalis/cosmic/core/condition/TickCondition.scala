package fr.unice.modalis.cosmic.core.condition


/**
 * Tick condition
 * @constructor frequency
 */
case class TickCondition(freq: Int) extends Condition {
  val frequency: Int = freq

  override def toString(): String = "TICK: " + freq
}