package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.core.Behavior

/**
 * Syntaxic sugar for Sensor Platform code generation
 */
object ToSensorPlatform {

  def apply(b:Behavior) = ToArduino(b)

}

/**
 * Syntaxic sugar for Bridge code generation
 */
object ToBridge {

  def apply(b:Behavior) = ToRaspberry(b)

}