package net.nomadicalien.ch2

import java.util.Date

/**
  * Pattern matching on ADTs
  */
object Listing2_9 {
  //Account abstraction similar to what you implemented before
  case class Account(no: String, name: String, dateOfOpening: Date,
    balance: Balance)

  //Instrument abstraction expressed as combination of sum and product types
  sealed trait Instrument

  case class Equity(isin: String, name: String, dateOfIssue: Date)
    extends Instrument

  case class FixedIncome(isin: String, name: String, dateOfIssue: Date,
    issueCurrency: Currency, nominal: BigDecimal) extends Instrument

  sealed trait Currency extends Instrument
  case object USD extends Currency
  case object JPY extends Currency

  //Helper class
  case class Amount(a: BigDecimal, c: Currency) {
    def +(that: Amount) = {
      require(that.c == c)
      Amount(a + that.a, c)
    }
  }

  // Balance abstraction that stores client balance for various types of Instrument
  case class Balance(amount: BigDecimal, ins: Instrument, asOf: Date)


  def getMarketValue(e: Equity, a: BigDecimal): Amount = ???
  def getAccruedInterest(i: String): Amount = ???

  /**
    * Pattern matching on a specific Instrument type (Currency and Equity)
   */
  def getHolding(account: Account): Amount = account.balance match {
    case Balance(a, c: Currency, _) => Amount(a, c)
    case Balance(a, e: Equity, _)   => getMarketValue(e, a)
    case Balance(a, FixedIncome(i, _, _, c, n), _) => //pattern matching and destructuring to extract values to be used in further computation
      Amount(n * a, c) + getAccruedInterest(i)
  }

}
