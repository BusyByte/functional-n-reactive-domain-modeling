package net.nomadicalien.ch2

import net.nomadicalien.ch2.Listing2_1.InterestRate

import scala.concurrent.Future

/**
  * Managing latency as effects in Scala
  */
object Listing2_14 {
  import scala.concurrent.ExecutionContext.Implicits.global

  sealed trait Account
  final case class SavingsAccount(rate: InterestRate) extends Account

  def calculateInterest[A <: SavingsAccount](account: A,
    balance: BigDecimal): Future[BigDecimal] = Future {
    if (account.rate == 0) throw new Exception("Interest Rate not found")
    else BigDecimal(10000)
  }

  def getCurrencyBalance[A <: SavingsAccount](account: A) :Future[BigDecimal] = Future {
    BigDecimal(1000L)
  }

  def calculateNetAssetValue[A <: SavingsAccount](account: A,
    ccyBalance: BigDecimal, interest: BigDecimal): Future[BigDecimal] =
    Future {
      ccyBalance + interest + 200
  }

}
