package net.nomadicalien.ch1

import java.util.Date

/**
  * Composing identity verification and opening of an account
  */
object Listing1_11 {
  import Listing1_10.AccountService._
  val date = new Date()
  val cust = getCustomer(1)
  verifyCustomer(cust)
    .map(c => openCheckingAccount(c, date))
    .getOrElse(throw new Exception("Verification failed for customer"))
}
