package net.nomadicalien.ch1

import java.util.Date

/**
  * Sample model from the personal banking domain
  */
object Listing1_4 {
  type Amount = BigDecimal
  case class Balance(amount: Amount = 0)

  /**
    * Aggregate
    */
  class Account(val no: String, val name: String, val dateOfOpening: Date) {
    var balance: Balance = Balance() //Mutable state in aggregate

    // Operations that mutate state
    def debit(a: Amount) = {
      if (balance.amount < a)
        throw new Exception("Insufficient balance in account")
      balance = Balance(balance.amount - a)
    }
    def credit(a: Amount) = {
      balance = Balance(balance.amount + a)
    }
  }
  val a = new Account("a1", "John", new Date())
  a.balance == Balance(0)
  a.credit(100)
  a.balance == Balance(100)
  a.debit(20)
  a.balance == Balance(80)
}
