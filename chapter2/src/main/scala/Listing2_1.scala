package net.nomadicalien.ch2

import java.util.Date

import scala.util.{Failure, Success, Try}

/**
  * Calculating interest for a customer account
  */
object Listing2_1 {
  sealed trait AccountType
  case object SAVINGS extends AccountType

  sealed trait AccountLevel
  case object SILVER   extends AccountLevel
  case object GOLD     extends AccountLevel
  case object PLATINUM extends AccountLevel

  sealed trait Month
  case object January   extends Month
  case object February  extends Month
  case object March     extends Month
  case object April     extends Month
  case object May       extends Month
  case object June      extends Month
  case object July      extends Month
  case object August    extends Month
  case object September extends Month
  case object October   extends Month
  case object November  extends Month
  case object December  extends Month

  type Year = Int

  type Amount       = BigDecimal
  type InterestRate = BigDecimal

  case class MonthlyAverageBalance(year: Year, month: Month, averageBalance: Amount)

  final case class Account(accountType: AccountType, accountLevel: AccountLevel, balances: List[MonthlyAverageBalance])
  final case class DateRange(from: Date, to: Date)

  def balanceInRange(balance: MonthlyAverageBalance, dateRange: DateRange): Boolean = ???

  def calculateInterest(account: Account, period: DateRange): Try[Amount] = {
    if (!(account.accountType == SAVINGS))
      Failure(new Exception(s"$account has to be a savings account"))
    else {
      val rate: InterestRate = account.accountLevel match {
        case SILVER   => 1.0
        case GOLD     => 1.5
        case PLATINUM => 2.0
      }

      val balances = account.balances.filter(balanceInRange(_, period))
      val interest: Amount = balances.foldLeft(0.0: Amount) {
        case (acc, balance) => acc + (balance.averageBalance * rate)
      }

      Success(interest)
    }
  }

}
