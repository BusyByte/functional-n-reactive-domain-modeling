package net.nomadicalien.ch2

import scala.util.{Failure, Success}

/**
  * Sequential composition of futures
  */
object Listing2_15 {
  import Listing2_14._
  import scala.concurrent.ExecutionContext.Implicits.global

  val s4 = SavingsAccount(1.0)

  val result = for {
    b <- getCurrencyBalance(s4)
    i <- calculateInterest(s4, b)
    v <- calculateNetAssetValue(s4, b, i)
  } yield (v)

  result onComplete {
    case Success(v)  => // Success path of the future completion
    case Failure(ex) => // Failure path of the future completion
  }
}
