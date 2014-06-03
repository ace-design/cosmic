package fr.unice.modalis.cosmic.converter

import scala.io.Source

/**
 * Utils functions for action convertion
 */
object Utils {

  // Sensor registry location
  val SENSOR_MAPPING = "sensors/mapping.csv"

  /**
   * Read csv file function
   * @param file File to parse
   * @return Iterator of array strings
   */
  def csvReader(file:String):Iterator[Array[String]] =
  {
    Source.fromFile(file).getLines().drop(1).map(_.split(","))
  }

  /**
   * Find sensor assignement
   * @param name Sensor name
   * @return Assignement (or exception if not found)
   */
  def lookupSensorAssignment(name:String):String = {
    csvReader(SENSOR_MAPPING).find(a => a(0).equals(name)) match {
      case Some(s) => s(1)
      case None => throw new Exception("Sensor " + name + " not found")
    }
  }
}
