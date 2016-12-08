package net.nomadicalien

import scala.util.Try

/**
  * Side effects in functions
  */
object Listing1_9 {
  trait AccountService {
    def openCheckingAccount(customer: Customer, effectiveDate: Date) = {
      // does an identity verification and throws exception if not passed

      Verifications.verifyRecord(customer)
      //.. Account-opening logic omitted exception if not passed
      Account(accountNo, openingDate, customer.name, customer.address, ..)
    }
    //.. other service methods
  }
}
