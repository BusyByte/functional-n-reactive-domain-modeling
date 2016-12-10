package net.nomadicalien.ch2

/**
  * Calculating interest—a polymorphic version
  */
object Listing2_2 {
  import Listing2_1.Amount
  import Listing2_1.DateRange
  import Listing2_1.InterestRate
  import Listing2_1.balanceInRange
  import Listing2_1.MonthlyAverageBalance

  sealed trait Account {
    def number: String
    def name: String
    def balances: List[MonthlyAverageBalance]
  }

  final case class CheckingAccount(number: String, name: String, balances: List[MonthlyAverageBalance]) extends Account

  sealed trait InterestBearingAccount extends Account {
    def rateOfInterest: InterestRate
  }

  final case class SavingsAccount(number: String, name: String, balances: List[MonthlyAverageBalance])
      extends InterestBearingAccount {
    lazy val rateOfInterest: InterestRate = 1.0
  }
  final case class MoneyMarketAccount(number: String, name: String, balances: List[MonthlyAverageBalance])
      extends InterestBearingAccount {
    lazy val rateOfInterest: InterestRate = 1.5
  }

  def calculateInterest[A <: InterestBearingAccount](account: A, period: DateRange): Amount = {
    val balances = account.balances.filter(balanceInRange(_, period))
    val interest: Amount = balances.foldLeft(0.0: Amount) {
      case (acc, balance) => acc + (balance.averageBalance * account.rateOfInterest)
    }

    interest
  }

}
