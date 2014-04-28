package fr.unice.modalis.fsm.actions.constraints

/**
 * Created by cyrilcecchinel on 28/04/2014.
 */
case class ORConstraint(val left:Constraint, val right:Constraint) extends Constraint{
  override def toString():String = left + " OR " + right
}
