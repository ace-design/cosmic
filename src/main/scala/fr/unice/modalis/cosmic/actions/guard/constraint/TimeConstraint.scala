package fr.unice.modalis.cosmic.actions.guard.constraint

import org.joda.time.DateTime
import java.text.SimpleDateFormat
import fr.unice.modalis.cosmic.algo.Utils

/**
 * Time constrain
 * @constructor Build a time-interval (24h format) constrain
 * @param begin begin time (hh:mm)
 * @param end end time (hh:mm)
 * @throws Exception when time format isn't valid
 */
case class TimeConstraint(val begin: DateTime, val end: DateTime) extends Constraint {

  val f: SimpleDateFormat = new SimpleDateFormat("HH:MM")

  def this(begin:String, end:String) = this(Utils.parseTime(begin), Utils.parseTime(end))

  override def toString(): String = begin + " to " + end


}
