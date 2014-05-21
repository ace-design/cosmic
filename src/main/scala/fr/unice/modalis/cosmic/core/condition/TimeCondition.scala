package fr.unice.modalis.cosmic.core.condition

import java.text.SimpleDateFormat

/**
 * TimeCondition
 * @param time String representing Time (format hh:mm 24-hours)
 * @throws Exception if time value isn't parsable
 */
case class TimeCondition(time: String) extends Condition {
  val f: SimpleDateFormat = new SimpleDateFormat("HH:MM");
  try {
    f.parse(time)
  } catch {
    case e: Exception => throw e
  }

  val hour: String = {
    time.split(":")(0)
  }
  val minute: String = {
    time.split(":")(1)
  }

  override def toString(): String = "TIME: " + hour + ":" + minute
}
