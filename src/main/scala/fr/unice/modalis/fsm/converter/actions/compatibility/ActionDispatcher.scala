package fr.unice.modalis.fsm.converter.actions.compatibility

import fr.unice.modalis.fsm.actions.unit._
import fr.unice.modalis.fsm.actions.unit.EmitAction
import fr.unice.modalis.fsm.actions.unit.ReadSensorAction
import fr.unice.modalis.fsm.actions.unit.SerialInitAction
import fr.unice.modalis.fsm.converter.actions.compatibility.Plateform._

/**
 * Action dispatcher
 * Dispatch actions on the different sensor network components
 */
object ActionDispatcher {

  def apply(l: Iterable[Action], p: Plateform) = dispatch(l, p)

  /**
   * Dispatch actions
   * @param l Action list
   * @param p Platform targeted
   * @return Two lists. The first one represent compatible action on the targeted plateform, the second one the incompatible ones
   */
  def dispatch(l: Iterable[Action], p: Plateform) = {
    if (p == BOARD) l.partition(a => boardCompatible(a))
    else if (p == BRIDGE) l.partition(a => bridgeCompatible(a))
    else l.partition(a => allCompatible(a))
  }

  /**
   * Compatibility rules for boards
   * @param a Action
   * @return Action compatibility issue on a board
   */
  private def boardCompatible(a: Action) = {
    a match {
      case _: SerialInitAction => false
      case _: ReadSensorAction => true
      case _: EmitAction => false
      case _: SendAction => true
      case _ => false
    }
  }

  /**
   * Compatibility rules for bridges
   * @param a Action
   * @return Action compatibility issue on a bridge
   */
  private def bridgeCompatible(a: Action) = {
    a match {
      case _: SerialInitAction => true
      case _: ReadSensorAction => false
      case _: EmitAction => true
      case _: SendAction => true
      case _ => false
    }
  }

  /**
   * Compatibility rules for all platforms
   * @param a Action
   * @return Action compatibility issue for all platforms
   */
  def allCompatible(a: Action) = {
    boardCompatible(a) && bridgeCompatible(a)
  }
}
