package net.nomadicalien

/**
  * Composing identity verification and opening of an account
  */
object Listing1_11 {
  import AccountService._
  val cust = getCustomer(..)
  verifyCustomer(cust).map(c => openCheckingAccount(c, date))
    .getOrElse(throw new Exception(
      "Verification failed for customer"))
}
