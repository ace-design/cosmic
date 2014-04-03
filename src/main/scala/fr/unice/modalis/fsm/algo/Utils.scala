package fr.unice.modalis.fsm.algo

/**
 * Utils methods
 */
object Utils {

  /**
   * Compute the gcd
   * @param a a
   * @param b b
   * @return The greatest common divisor between a and b
   */
  def gcd(a: Int, b: Int):Int=if (b==0) a.abs else gcd(b, a%b)

  /**
   * Compute the lcd
   * @param a a
   * @param b b
   * @return The least commin divisor between a and b
   */
  def lcm(a: Int, b: Int)=(a*b).abs/gcd(a,b)

  /**
   * Compute lcd over a number list
   * @param args Int list
   * @return The greatest common divisor between each number
   */
  def lcmm(args:List[Int]):Int =
    args match {
      case a::b::Nil => lcm(a,b)
      case a::b::tail => lcmm(lcm(a,b)::tail)
    }
}
