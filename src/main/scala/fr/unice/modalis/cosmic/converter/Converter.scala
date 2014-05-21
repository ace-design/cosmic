package fr.unice.modalis.cosmic.converter

import fr.unice.modalis.cosmic.core.Behavior

/**
 * Abstract class Converter
 * Contain common methods needed for all converters
 */
trait Converter {

  def apply(b: Behavior): String = generateCode(b)

  def generateCode(b: Behavior): String
}
