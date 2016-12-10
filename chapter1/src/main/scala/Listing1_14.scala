package net.nomadicalien.ch1

import scala.concurrent.Future
import scala.concurrent.Future.successful

/**
  * Portfolio statement—event driven
  */
object Listing1_14 {
  type Balance   = BigDecimal
  type Portfolio = (Balance, Balance, Balance, Balance, Balance)
  def getCurrencyBalance(): Future[Balance]       = successful(1)
  def getEquityBalance(): Future[Balance]         = successful(2)
  def getDebtBalance(): Future[Balance]           = successful(3)
  def getLoanInformation(): Future[Balance]       = successful(4)
  def getRetirementFundBalance(): Future[Balance] = successful(5)
  def generatePortfolio(c: Balance, e: Balance, d: Balance, l: Balance, r: Balance): Portfolio =
    (c, e, d, l, r)

  import scala.concurrent.ExecutionContext.Implicits.global

  val profolioF: Future[Portfolio] = for {
    curr: Balance   <- getCurrencyBalance()
    eq: Balance     <- getEquityBalance()
    debt: Balance   <- getDebtBalance()
    loan: Balance   <- getLoanInformation()
    retire: Balance <- getRetirementFundBalance()
  } yield generatePortfolio(curr, eq, debt, loan, retire)

}
