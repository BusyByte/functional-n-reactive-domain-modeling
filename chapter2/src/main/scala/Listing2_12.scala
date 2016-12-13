package net.nomadicalien.ch2

import net.nomadicalien.ch2.Listing2_11._

/**
  * A concrete module for interest computation
  */
object Listing2_12 {

  /*
    Note the type and value members that were abstract in the
    traits TaxCalculationTable and TaxCalculation have been
    made concrete in the respective object declarations.

    The core takeaway from this strategy is that you don’t need to
    commit to the exact type or value until you declare the object.

    This is awesome for module composition.
   */
  object InterestTaxCalculationTable extends TaxCalculationTable {
    type T = TransactionType
    val transactionType = InterestComputation
  }
  object TaxCalculation extends TaxCalculation {
    type S = TaxCalculationTable
    val table = InterestTaxCalculationTable
  }
  object InterestCalculation extends InterestCalculation {
    type C = TaxCalculation
    val taxCalculation = TaxCalculation
  }

}
