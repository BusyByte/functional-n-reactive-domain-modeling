package net.nomadicalien.ch2

import net.nomadicalien.ch2.Listing2_1.InterestRate

import scala.util.{Failure, Success, Try}

/**
  * Managing failures in Scala
  */
object Listing2_13 {

  sealed trait Account
  final case class SavingsAccount(rate: InterestRate) extends Account

  def calculateInterest[A <: SavingsAccount](account: A,
    balance: BigDecimal):Try[BigDecimal] = {
    if (account.rate == 0) Failure(new Exception("Interest Rate not found"))
    else Success(BigDecimal(10000))
  }
  def getCurrencyBalance[A <: SavingsAccount](account: A):Try[BigDecimal] = {
    Success(BigDecimal(1000L))
  }
  def calculateNetAssetValue[A <: SavingsAccount](account: A,
    ccyBalance: BigDecimal, interest: BigDecimal): Try[BigDecimal] = {
    Success(ccyBalance + interest + 200)
  }

}
