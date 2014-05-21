package fr.unice.modalis.cosmic.algo.vm

import fr.unice.modalis.cosmic.core.Behavior

trait Instruction {
  def make(b: Behavior): Behavior

  override def toString(): String
}

