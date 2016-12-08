package net.nomadicalien

/**
  * Function composition and higher-order functions
  */
object Listing1_7 {
  //TODO: demo with unit test rather than comments
  //Function that adds 1 to an integer
  val inc1 = (n: Int) => n + 1 // inc1: Int => Int = <function1>

  //Function that squares its integer input
  val square = (n: Int) => n * n // square: Int => Int = <function1>

  //Map the increment function over a collection.
  (1 to 10) map inc1
  //res1: scala.collection.immutable.IndexedSeq[Int] = ➥ Vector(2, 3, 4, 5, 6, 7, 8, 9, 10, 11)

  //Map the square function over a collection.
  (1 to 10) map square
  //res4: scala.collection.immutable.IndexedSeq[Int] = ➥ Vector(1, 4, 9, 16, 25, 36, 49, 64, 81, 100)

  //Define a function composition (add 1, then square).
  val incNSquare = inc1 andThen square // incNSquare: Int => Int = <function1>

  incNSquare(4) // res6: Int = 25

  //Another way to define composition (square, then add 1)
  val squareNInc = inc1 compose square
  //squareNInc: Int => Int = <function1>

  squareNInc(4) // res8: Int = 17
}
