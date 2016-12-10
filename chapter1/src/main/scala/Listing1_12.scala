package net.nomadicalien.ch1

/**
  * The substitution model of evaluation
  */
object Listing1_12 {
  def f(a: Int)                      = sum_of_squares(a + 1, a * 2)
  def sum_of_squares(a: Int, b: Int) = square(a) + square(b)
  def square(a: Int)                 = a * a
  /*
  ■ f(5)
  ■ sum_of_squares(5 + 1, 5 * 2)
  ■ square(6) + square(10)
  ■ 6*6+10*10
  ■ 36+100
  ■ 136
 */
}
