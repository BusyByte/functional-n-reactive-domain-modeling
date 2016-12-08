package net.nomadicalien

/**
  * Portfolio statement
  */
object Listing1_13 {
  type Balance = BigDecimal
  type Portfolio = (Balance, Balance, Balance, Balance, Balance)
  def getCurrencyBalance(): Balance = 1
  def getEquityBalance(): Balance = 2
  def getDebtBalance(): Balance = 3
  def getLoanInformation(): Balance = 4
  def getRetirementFundBalance(): Balance = 5
  def generatePortfolio(c: Balance, e: Balance, d: Balance, l: Balance, r: Balance): Portfolio =
    (c, e, d, l, r)
  val curr: Balance = getCurrencyBalance()
  val eq: Balance = getEquityBalance()
  val debt: Balance = getDebtBalance()
  val loan: Balance = getLoanInformation()
  val retire: Balance = getRetirementFundBalance()
  val portfolio = generatePortfolio(curr, eq, debt, loan, retire)
}
