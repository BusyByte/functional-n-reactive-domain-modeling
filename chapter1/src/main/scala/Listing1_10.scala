package net.nomadicalien.ch1

import java.util.{Date, UUID}

import net.nomadicalien.ch1.Listing1_6.Account

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

    /**
      * NOTE: This is not as good as it could be.
      * This could still let you open a checking account for a non-verified
      * Customer.
      * Would be better to:
      * - have openCheckingAccount take a VerifiedCustomer
      * - have verifyCustomer return an Option[VerifiedCustomer]
      *
      * This way types can be used to assure an account can never be opened for a non-verified customer
      */
    def openCheckingAccount(customer: Customer, effectiveDate: Date) = {
      //.. Account-opening logic
      val accountNo   = UUID.randomUUID().toString
      val openingDate = effectiveDate
      Account(accountNo, customer.name, openingDate)
    }
  }
  object AccountService extends AccountService
}
