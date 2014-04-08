package fr.unice.modalis.fsm.condition

import java.text.SimpleDateFormat
import java.util.{GregorianCalendar, Calendar}

/**
 * TimeCondition
 * @param time String representing Time (format hh:mm 24-hours)
 * @throws Exception if time value isn't parsable
 */
class TimeCondition(time:String) extends Condition{
   val f:SimpleDateFormat = new SimpleDateFormat("HH:MM");
   try {
     f.parse(time)
   } catch {
     case e: Exception => throw e
   }

  val hour:String = {time.split(":")(0)}
  val minute:String = {time.split(":")(1)}

  override def toString():String = "TIME: " + hour + ":" + minute
}
