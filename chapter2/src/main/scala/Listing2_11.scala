package net.nomadicalien.ch2

/**
  * Interest calculation modules and their dependencies
  */
object Listing2_11 {

  sealed trait TaxType
  case object Tax extends TaxType
  case object Fee extends TaxType
  case object Commission extends TaxType

  sealed trait TransactionType
  case object InterestComputation extends TransactionType
  case object Dividend extends TransactionType

  type Amount = BigDecimal

  case class Balance(amount: Amount = 0)

  // Tax calculation table parameterized on transaction type
  trait TaxCalculationTable {
    val transactionType: T
    type T <: TransactionType
    def getTaxRates: Map[TaxType, Amount] = ???
  }

  // Tax calculation logic parameterized on tax calculation table
  trait TaxCalculation {
    val table: S
    type S <: TaxCalculationTable
    def calculate(taxOn: Amount): Amount =
      table.getTaxRates.map { case (t, r) =>
      doCompute(taxOn, r)
      }.sum

    protected def doCompute(taxOn: Amount, rate: Amount): Amount = {
      taxOn * rate
    }
  }

  // Another specialization of tax calculation for a specific geography that has additional tax to be levied
  trait SingaporeTaxCalculation extends TaxCalculation {
    def calculateGST(tax: Amount, gstRate: Amount) =
      tax * gstRate
  }

  // Interest calculation logic parameterized on tax calculation logic
  trait InterestCalculation {
    type C <: TaxCalculation
    val taxCalculation: C
    def interest(b: Balance): Option[Amount] = Some(b.amount * 0.05)
    def calculate(balance: Balance): Option[Amount] =
      interest(balance).map { i =>
        i - taxCalculation.calculate(i)
      }
  }



}
