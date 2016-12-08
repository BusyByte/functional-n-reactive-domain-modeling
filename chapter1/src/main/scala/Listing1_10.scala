package net.nomadicalien

import java.util.{Date, UUID}

import net.nomadicalien.Listing1_6.Account

/**
  * Decoupling side effects
  */
object Listing1_10 {
  import Listing1_9.Customer

  trait Verifications {
    def verifyRecord(customer: Customer): Boolean
  }
  object Verifications extends Verifications {
    def verifyRecord(customer: Customer): Boolean =
      customer.address.state.length == 2
  }

  trait AccountService {
    def getCustomer(customerId: Int): Customer = ???
    def verifyCustomer(customer: Customer): Option[Customer] = {
      if (Verifications.verifyRecord(customer)) Some(customer)
      else None
    }
    def openCheckingAccount(customer: Customer, effectiveDate: Date) = {
      //.. Account-opening logic
      val accountNo   = UUID.randomUUID().toString
      val openingDate = effectiveDate
      Account(accountNo, customer.name, openingDate)
    }
  }
  object AccountService extends AccountService
}
