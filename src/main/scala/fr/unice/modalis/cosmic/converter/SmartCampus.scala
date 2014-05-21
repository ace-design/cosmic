package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.core.Behavior

/**
 * Syntaxic sugar for Sensor Platform code generation
 */
object ToSensorPlatform extends Converter{

  def generateCode(b: Behavior): String = ToArduino(b)
}

/**
 * Syntaxic sugar for Bridge code generation
 */
object ToBridge extends Converter {

  def generateCode(b: Behavior): String = ToRaspberry(b)

}