package fr.unice.modalis.fsm.actions.constraints

/**
 * Action constrain parent class
 */
abstract class Constraint() {
  override def toString():String
  override def equals(x:Any):Boolean
}
