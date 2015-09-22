package fr.unice.modalis.cosmic.converter

import java.util.Calendar

import fr.unice.modalis.cosmic.actions.guard.GuardAction
import fr.unice.modalis.cosmic.actions.unit.{Action, Variable}
import fr.unice.modalis.cosmic.core.{Behavior, Node, Transition}

import scala.io.Source

/**
 * Code generator trait
 */
trait CodeGenerator {

  /* TO IMPLEMENT */


  // Path to template file
  val templateFile: String

  /**
   * Translate an action into the target language
   * @param a Action
   * @return Action translated
   */
  def translateAction(a:Action):(String,Set[Variable]) //Action translator

  /**
   * Translate a transition into the target language
   * @param t Transition
   * @return Transition translated
   */
  def translateTransition(t:Transition):String //Transition translator

  /**
   * Translate a guard into the target language
   * @param g Guard
   * @return Guard translated
   */
  def translateGuard(g:GuardAction):String //Guard translator

  /**
   * Build an action into the target language
   * @param a Action
   * @return Action builded
   */
  def buildAction(a:Action):(String,Set[Variable]) //Action builder (ie. Action + guard)

  /**
   * Build a guard condition into the target language
   * @param l Guard list
   * @return Guard condition
   */
  def buildGuards(l:List[GuardAction]):String //Guards builder

  /**
   * Build variable list into the target language
   * @return Variable list
   */
  def buildVariables():String //Variables builder
  /* ************* */

  /* Framework methods */
  def apply(b:Behavior) = generate(b)
  var variables: Set[Variable] = Set[Variable]()


  def buildBehavior(b:Behavior):String = {
    val behavior: StringBuilder = new StringBuilder
    for (i <- 0 to b.period() - 1) {
      if (b.newNodeAt(i)) {
        val currentNode = b.nodeAt(i)

        // Generate actions
        behavior.append(generateNodeCode(currentNode))

        // Generate transition
        behavior.append(generateTransitionCode(b.transitions.filter(t => t.source == currentNode).head))
      }
    }
    behavior.toString()
  }

  def generateNodeCode(n:Node) = {
    val str = new StringBuilder
    n.actions.actions.foreach(a => {
      val r = buildAction(a)
      str.append(r._1 + "\n")
      variables = variables ++ r._2
    })

    str.toString()
  }

  def generateTransitionCode(t:Transition) = {
    translateTransition(t) + "\n"
  }

  def replace(parameter:String, value:String, source:String):String = source.replace("#@" + parameter + "@#", value)

  def generate(b:Behavior):String = {
    var generatedCode = Source.fromFile(templateFile).getLines().mkString("\n")
    generatedCode = replace("time", Calendar.getInstance().getTime.toString, generatedCode)
    generatedCode = replace("behavior", buildBehavior(b), generatedCode)
    generatedCode = replace("variables", buildVariables(), generatedCode)

    generatedCode
  }


}