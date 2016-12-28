package net.nomadicalien.ch4

import java.util.Date

import net.nomadicalien.ch4.Listing_4_1.{Balance, DR, Money, Transaction}


/**
  * Base abstractions for defining Transaction and Balance
  * page 112
  */
object Listing_4_1 {

  /**
    * Transaction type—can be debit (DR) or credit (CR)
    */
  sealed trait TransactionType
  case object DR extends TransactionType
  case object CR extends TransactionType

  /**
    * Currency and its enumerations
    */
  sealed trait Currency
  case object USD extends Currency
  case object JPY extends Currency
  case object AUD extends Currency
  case object INR extends Currency


  /**
    * Money abstraction. You have a Map to encode denominations of multiple currencies.
    */
  case class Money(m: Map[Currency, BigDecimal]) {
    def +(that: Money) = {
      val n = that.m.foldLeft(m) { (a, e) =>
        val (ccy, amt) = e
        a.get(ccy).map { amount =>
          a + ((ccy, amt + amount))
        }.getOrElse(a + ((ccy, amt)))
      }
      Money(n)
    }
    def toBaseCurrency: BigDecimal = ???
  }

  object Money {
    val zeroMoney = Money(Map.empty[Currency, BigDecimal])
  }

  object MoneyOrdering extends Ordering[Money] {
    def compare(a:Money, b:Money) = a.toBaseCurrency compare b.toBaseCurrency
  }

  /**
    * Transaction that clients make in a bank
    */
  case class Transaction(txid: String, accountNo: String, date: Date, amount: Money, txnType: TransactionType, status: Boolean)

  /**
    * Balance of a client
    */
  case class Balance(b: Money)
}


/**
  * Algebra and implementation of module Analytics
  * page 112
  */
object Listing_4_2 {

  import Listing_4_1._
  import Money.zeroMoney

  /**
    * Algebra of the module
    */
  trait Analytics[Transaction, Balance, Money] {
    def maxDebitOnDay(txns: List[Transaction]): Money

    def sumBalances(bs: List[Balance]): Money
  }


  object Analytics extends Analytics[Transaction, Balance, Money] {

    import scala.math.Ordering
    import MoneyOrdering._

    /**
      * Ordering defined on Money is elided. Check the online code repo.
      */
    def maxDebitOnDay(txns: List[Transaction]): Money = {
      txns.filter(_.txnType == DR).foldLeft(zeroMoney) { (a, txn) =>

        if (gt(txn.amount, a)) valueOf(txn) else a
      }
    }

    /**
      * Addition of Money elided. Check online repo for details.
      */
    def sumBalances(balances: List[Balance]): Money = {
      balances.foldLeft(zeroMoney) { (a, b) =>
        a + creditBalance(b)
      }
    }

    /**
      * valueOf finds the monetary value of a transaction.
      */
    private def valueOf(txn: Transaction): Money = ???

    /**
      * Returns the balance only if it’s a credit balance; otherwise, returns 0.
      */
    private def creditBalance(b: Balance): Money = ???


  }

}

/**
  * Abstracting operations through a monoid
  * page 114
  */
object Listing_4_3 {
  trait Monoid[T] {
    def zero: T
    def op(t1: T, t2: T): T
  }
  /**
    * Both functions now need an implicit Monoid for Money.
    * This will be used in the implementation to transform specific operations on Money to that of a Monoid.
    * This makes those operations reusable for any Monoid, not only Money.
    */
  trait Analytics[Transaction, Balance, Money] {

    def maxDebitOnDay(txns: List[Transaction])(implicit m: Monoid[Money]): Money
    def sumBalances(bs: List[Balance])(implicit m: Monoid[Money]): Money
  }

  object Analytics extends Analytics[Transaction, Balance, Money] {
    def maxDebitOnDay(txns: List[Transaction])(implicit m: Monoid[Money]): Money = {
      txns.filter(_.txnType == DR).foldLeft(m.zero) { (a, txn) =>
      m.op(a, valueOf(txn)) }
    }
    def sumBalances(balances: List[Balance])(implicit m: Monoid[Money]): Money =
      balances.foldLeft(m.zero) { (a, bal) => m.op(a, creditBalance(bal)) }

    private def valueOf(txn: Transaction): Money = ???
    private def creditBalance(b: Balance): Money = ???
  }
}