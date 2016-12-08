package net.nomadicalien

import java.util.Date

/**
  * Immutable Account model
  */
object Listing1_5 {

  type Amount = BigDecimal

  case class Balance(amount: Amount = 0)

  class Account(
    val no: String,
    val name: String,
    val dateOfOpening: Date,
    val balance: Balance = Balance() //Balance is now immutable.
  ) {

    //The operations debit and credit create new instances of Account.
    def debit(a: Amount) = {
      if (balance.amount < a)
        throw new Exception("Insufficient balance in account")
      new Account(no, name, dateOfOpening, Balance(balance.amount - a))
    }
    def credit(a: Amount) =
      new Account(no, name, dateOfOpening, Balance(balance.amount + a))

  }

  //Immutability in action account balance isn’t mutated in place.
  val today = new Date()
  val a = new Account("a1", "John", today)
  a.balance == Balance(0)
  val b = a.credit(100)
  a.balance == Balance(0)
  b.balance == Balance(100)
  val c = b.debit(20)
  b.balance == Balance(100)
  c.balance == Balance(80)
}
