package fr.unice.modalis.fsm.actions.constraints

/**
 * Created by cyrilcecchinel on 28/04/2014.
 */
case class NOTConstraint(val expression:Constraint) extends Constraint{
  override def toString():String = "NOT(" + expression + ")"

}
