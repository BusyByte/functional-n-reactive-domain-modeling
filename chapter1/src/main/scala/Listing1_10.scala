package net.nomadicalien

/**
  * Decoupling side effects
  */
object Listing1_10 {
  trait AccountService {
    def getCustomer(customerId: Int): Customer
    def verifyCustomer(customer: Customer): Option[Customer] = {
      if (Verifications.verifyRecord(customer)) Some(customer)
      else None
    }
    def openCheckingAccount(customer: Customer, effectiveDate: Date) = {
      //.. Account-opening logic
      Account(accountNo, openingDate, customer.name, customer.address)
    }
    object AccountService extends AccountService
  }
}
