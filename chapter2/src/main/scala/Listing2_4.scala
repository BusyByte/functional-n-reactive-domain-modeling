package net.nomadicalien.ch2

import java.util.Calendar

import net.nomadicalien.ch2.Listing2_1.{Amount, DateRange}
import net.nomadicalien.ch2.Listing2_3.{Account, SavingsAccount}

import scala.util.Try

/**
  * Composition that leads to evolution of abstractions

  */
object Listing2_4 {

  import Listing2_3.AccountService._

  //Sample list of savings accounts
  val s1 = SavingsAccount("dg", "sb001")
  val s2 = SavingsAccount("sr", "sb002")
  val s3 = SavingsAccount("ty", "sb003")

  val dateRange = {
    val d1 = {
      val c = Calendar.getInstance()
      c.set(2016, Calendar.JANUARY, 1)
      c.getTime
    }
    val d2 = {
      val c = Calendar.getInstance()
      c.set(2017, Calendar.JANUARY, 1)
      c.getTime
    }
    DateRange(d1, d2)
  }

  // Maps over a list of savings accounts; computes interest for each of them
  List(s1, s2, s3).map(calculateInterest(_, dateRange))

  //Finds total interest accumulated in a list of savings accounts
  List(s1, s2, s3).map(calculateInterest(_, dateRange))
    .foldLeft(0: Amount)((a, e) => e.map(_ + a).getOrElse(a))

  // Gets list of interest calculated using the e filter combinator
  List(s1, s2, s3).map(calculateInterest(_, dateRange))
      .filter(_.isSuccess)

  def getCurrencyBalance(a: Account): Try[Amount] = ???
  def getAccountFrom(no: String): Try[Account] = ???
  def calculateNetAssetValue(a: Account, balance: Amount): Try[Amount] = ???

  // Lists net asset value from an account number if net asset value > 100,000
  val result: Try[(Account, Amount)] = for {
    s <- getAccountFrom("a1")
    b <- getCurrencyBalance(s)
    v <- calculateNetAssetValue(s, b)
    if (v > 100000)
  } yield (s, v)
}
