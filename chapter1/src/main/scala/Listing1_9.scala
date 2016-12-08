package net.nomadicalien

import java.util.{Date, UUID}

/**
  * Side effects in functions
  */
object Listing1_9 {
  import Listing1_2.Address
  import Listing1_6.Account

  final case class Customer(name: String, address: Address)

  trait Verifications {
    def verifyRecord(customer: Customer): Unit
  }
  object Verifications extends Verifications {
    def verifyRecord(customer: Customer): Unit =
      if (customer.address.state.length != 2) {
        throw new Exception("Need to use address abbreviation")
      }
  }

  trait AccountService {
    def openCheckingAccount(customer: Customer, effectiveDate: Date) = {
      // does an identity verification and throws exception if not passed

      Verifications.verifyRecord(customer)

      val accountNo   = UUID.randomUUID().toString
      val openingDate = effectiveDate
      Account(accountNo, customer.name, openingDate)
    }
    //.. other service methods
  }
}
