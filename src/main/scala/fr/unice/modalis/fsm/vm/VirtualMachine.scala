package fr.unice.modalis.fsm.vm

import fr.unice.modalis.fsm.core.Behavior

/**
 * Virtual Machine class
 * Compute new behaviors
 */
object VirtualMachine {
  /**
   * Apply an actions list upon a behavior
   * @param b Current behavior
   * @param arr Action list
   * @return A new behavior with the actions list applied
   */
  def apply(b:Behavior, arr:List[Instruction]):Behavior =
  {
    arr match {
      case x :: tail => apply(x.make(b), tail)
      case Nil => b
    }
  }
}
