package net.nomadicalien.ch1

import scala.util.Try

/**
  * Composition through higher-order functions
  */
object Listing1_8 {
  import Listing1_6._
  import AccountService._
  val generateAuditLog: (Account, Amount) => Try[String] = ???
  val source                                             = Account("a1", "John", today)
  val amount: Amount                                     = 100

  val write: String => Unit = ???
  debit(source, amount) // Debit the account. Debit returns a Try[Account], which you flatMap in #B.
    .flatMap(b => generateAuditLog(b, amount)) // If debit is okay, generate audit log
    .foreach(write) // If log generated, write to database
}
