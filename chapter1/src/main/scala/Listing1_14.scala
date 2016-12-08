package net.nomadicalien

import scala.concurrent.Future

/**
  * Portfolio statement—event driven
  */
object Listing1_14 {
  type Balance = BigDecimal
  type Portfolio = (Balance, Balance, Balance, Balance, Balance)
  def getCurrencyBalance(): Future[Balance] = 1
  def getEquityBalance(): Future[Balance] = 2
  def getDebtBalance(): Future[Balance] = 3
  def getLoanInformation(): Future[Balance] = 4
  def getRetirementFundBalance(): Future[Balance] = 5
  def generatePortfolio(c: Balance, e: Balance, d: Balance, l: Balance, r: Balance): Portfolio =
    (c, e, d, l, r)
  
  val profolioF: Future[Portfolio] = for {
     curr: Balance <- getCurrencyBalance()
     eq: Balance <- getEquityBalance()
     debt: Balance <- getDebtBalance()
     loan: Balance <- getLoanInformation()
     retire: Balance <- getRetirementFundBalance()
  } yield generatePortfolio(curr, eq, debt, loan, retire)

}
