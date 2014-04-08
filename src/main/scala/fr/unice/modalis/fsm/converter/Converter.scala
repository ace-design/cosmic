package fr.unice.modalis.fsm.converter

import fr.unice.modalis.fsm.core.Behavior

/**
 * Abstract class Converter
 * Contain common methods needed for all converters
 */
abstract  class Converter {
  def generateCode(b:Behavior):String
}
