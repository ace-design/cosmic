package fr.unice.modalis.cosmic.actions.guard.constraint

import java.text.SimpleDateFormat

/**
 * Time constrain
 * @constructor Build a time-interval (24h format) constrain
 * @param begin begin time (hh:mm)
 * @param end end time (hh:mm)
 * @throws Exception when time format isn't valid
 */
case class TimeConstraint(begin: String, end: String) extends Action {
  val f: SimpleDateFormat = new SimpleDateFormat("HH:MM")
  try {
    f.parse(begin)
    f.parse(end)
  } catch {
    case e: Exception => throw e
  }

  override def toString(): String = begin + " to " + end


}
