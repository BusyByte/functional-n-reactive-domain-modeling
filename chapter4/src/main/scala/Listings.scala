package net.nomadicalien.ch4

import java.util.Date

import net.nomadicalien.ch4.Listing_4_1._
import net.nomadicalien.ch4.Listing_4_3.Monoid

import scala.util.{Success, Try}
import scalaz.Kleisli

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
        a.get(ccy)
          .map { amount =>
            a + ((ccy, amt + amount))
          }
          .getOrElse(a + ((ccy, amt)))
      }
      Money(n)
    }
    def toBaseCurrency: BigDecimal = ???
  }

  object Money {
    val zeroMoney = Money(Map.empty[Currency, BigDecimal])
  }

  object MoneyOrdering extends Ordering[Money] {
    def compare(a: Money, b: Money) = a.toBaseCurrency compare b.toBaseCurrency
  }

  /**
    * Transaction that clients make in a bank
    */
  case class Transaction(txid: String,
                         accountNo: String,
                         date: Date,
                         amount: Money,
                         txnType: TransactionType,
                         status: Boolean)

  /**
    * Balance of a client
    */
  case class Balance(amount: Money)
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
        m.op(a, valueOf(txn))
      }
    }
    def sumBalances(balances: List[Balance])(implicit m: Monoid[Money]): Money =
      balances.foldLeft(m.zero) { (a, bal) =>
        m.op(a, creditBalance(bal))
      }

    private def valueOf(txn: Transaction): Money = ???
    private def creditBalance(b: Balance): Money = ???
  }
}

/**
  * The Applicative Functor trait (simplified) (page 122)
  */
object Listing_4_4 {
  trait Functor[F[_]] {
    def map[A, B](a: F[A])(f: A => B): F[B]
  }

  trait Applicative[F[_]] extends Functor[F] {
    /*
      Primitive operations that implementing classes need to provide.
      You’ll see a sample implementation shortly.
     */
    def ap[A, B](fa: => F[A])(f: => F[A => B]): F[B]
    def apply2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] =
      ap(fb)(map(fa)(f.curried))
    def lift2[A, B, C](f: (A, B) => C): (F[A], F[B]) => F[C] =
      apply2(_, _)(f)
    def unit[A](a: => A): F[A]

  }
}

/**
  * Balance update using the State monad (page 131)
  */
object Listing_4_5 {
  import Money.zeroMoney
  import scalaz.State
  import scalaz.State._
  type AccountNo = String
  type Balances  = Map[AccountNo, Balance]
  val balances: Balances = Map.empty[AccountNo, Balance]

  implicit val BigDecimalAdditionMonoid = new Monoid[BigDecimal] {
    val zero                             = BigDecimal(0)
    def op(i: BigDecimal, j: BigDecimal) = i + j
  }

  implicit def MapMonoid[K, V: Monoid] = new Monoid[Map[K, V]] {
    def zero = Map.empty[K, V]
    def op(m1: Map[K, V], m2: Map[K, V]) = m2.foldLeft(m1) { (a, e) =>
      val (key, value) = e
      a.get(key).map(v => a + ((key, implicitly[Monoid[V]].op(v, value)))).getOrElse(a + ((key, value)))
    }
  }

  implicit def MoneyAdditionMonoid = new Monoid[Money] {
    val m                        = implicitly[Monoid[Map[Currency, BigDecimal]]]
    def zero                     = zeroMoney
    def op(m1: Money, m2: Money) = Money(m.op(m1.m, m2.m))
  }

  implicit def BalanceAdditionMonoid = new Monoid[Balance] {
    val m                            = implicitly[Monoid[Money]]
    def zero                         = Balance(zeroMoney)
    def op(b1: Balance, b2: Balance) = Balance(m.op(b1.amount, b2.amount))
  }

  def updateBalance(txns: List[Transaction]): State[Balances, Unit] =
    modify { (b: Balances) =>
      txns.foldLeft(b) { (a, txn) =>
        implicitly[Monoid[Balances]].op(a, Map(txn.accountNo -> Balance(txn.amount)))
      }
    }

  val txns = List.empty[Transaction]
  updateBalance(txns) run balances
}

/**
  * Using a monadic combinator to generate valid account numbers
  */
object Listing_4_6 {
  import scalaz._
  //import Scalaz._

  sealed trait Account
  sealed trait AccountRepository {
    def query(accountNo: String): Try[Option[Account]]
  }
  /*
    AccountRepository is the repository, which stores accounts.
    See chapter 3 for details on Repository.
   */
  final class Generator(rep: AccountRepository) {
    //The generation logic will be complex. Assume random strings for the time being.
    val no: String = scala.util.Random.nextString(10)
    def exists: Boolean = rep.query(no) match { // Queries the repository to check for uniqueness
      case scala.util.Success(Some(_)) => true
      case _                           => false
    }
  }

  val StateGen = StateT.stateMonad[Generator]
  import StateGen._
  val r: AccountRepository = ???
  val s                    = whileM_(gets(_.exists), modify(_ => new Generator(r)))
  val start                = new Generator(r)
  s exec start
}

/**
  * Algebra for the Trading API (page 140)
  */
object Listing_4_7 {
  trait Trading[Account, Market, Order, ClientOrder, Execution, Trade] {
    def clientOrders: ClientOrder => List[Order]
    def execute: Market => Account => Order => List[Execution]
    def allocate: List[Account] => Execution => List[Trade]
  }
}

/**
  * Algebra for the Trading API (slightly refactored) (page 141)
  */
object Listing_4_8 {
  trait Trading[Account, Market, Order, ClientOrder, Execution, Trade] {
    def clientOrders: ClientOrder => List[Order]
    def execute(m: Market, a: Account): Order => List[Execution]
    def allocate(as: List[Account]): Execution => List[Trade]
  }
}

/**
  * Algebra for the Trading API (using the Kleisli pattern) (page 142)
  */
object Listing_4_9 {
  trait Trading[Account, Market, Order, ClientOrder, Execution, Trade] {
    def clientOrders: Kleisli[List, ClientOrder, Order]
    def execute(m: Market, a: Account): Kleisli[List, Order, Execution]
    def allocate(as: List[Account]): Kleisli[List, Execution, Trade]
  }
}

/**
  * Trade generation from client orders (page 143)
  */
object Listing_4_10 {
  import scalaz._
  import Scalaz._

  trait Trading[Account, Market, Order, ClientOrder, Execution, Trade] {
    def clientOrders: Kleisli[List, ClientOrder, Order]
    def execute(m: Market, a: Account): Kleisli[List, Order, Execution]
    def allocate(as: List[Account]): Kleisli[List, Execution, Trade]

    def tradeGeneration(
        market: Market,
        broker: Account,
        clientAccounts: List[Account]
    ): Kleisli[List, ClientOrder, Trade] = {
      clientOrders andThen
        execute(market, broker) andThen
        allocate(clientAccounts)
    }
  }
}

/**
  * The model for a loan application (page 144)
  */
object Listing_4_11 {
  object Loans {

    case class LoanApplication private[Loans] (
        date: Date,
        name: String,
        purpose: String,
        repayIn: Int,
        actualRepaymentYears: Option[Int] = None,
        startDate: Option[Date] = None,
        loanNo: Option[String] = None,
        emi: Option[BigDecimal] = None
    )

  }
}

/**
  * First draft of workflow functions for loan processing (page 145)
  */
object Listing_4_12 {
  object Loans {

    case class LoanApplication private[Loans] (
        date: Date,
        name: String,
        purpose: String,
        repayIn: Int,
        actualRepaymentYears: Option[Int] = None,
        startDate: Option[Date] = None,
        loanNo: Option[String] = None,
        emi: Option[BigDecimal] = None
    )

    def applyLoan(name: String, purpose: String, repayIn: Int, date: Date): LoanApplication = ???
    def approve: Kleisli[Option, LoanApplication, LoanApplication]                          = ???
    def enrich: Kleisli[Option, LoanApplication, LoanApplication]                           = ???
  }
}

/**
  * The loan-processing workflow with phantom types
  */
object Listing_4_13 {
  object Loans {
    import scalaz._
    import scalaz.Scalaz._

    val today = new Date()

    //LoanApplication is now parameterized on the type
    case class LoanApplication[Status] private[Loans] (
        date: Date,
        name: String,
        purpose: String,
        repayIn: Int,
        actualRepaymentYears: Option[Int] = None,
        startDate: Option[Date] = None,
        loanNo: Option[String] = None,
        emi: Option[BigDecimal] = None
    )

    // The Phantom types
    trait Applied

    trait Approved

    trait Enriched

    type LoanApplied  = LoanApplication[Applied]
    type LoanApproved = LoanApplication[Approved]
    type LoanEnriched = LoanApplication[Enriched]

    def applyLoan(name: String, purpose: String, repayIn: Int, date: Date = today) =
      LoanApplication[Applied](date, name, purpose, repayIn)

    def approve = Kleisli[Option, LoanApplied, LoanApproved] { l =>
      l.copy(
          loanNo = scala.util.Random.nextString(10).some,
          actualRepaymentYears = 15.some,
          startDate = today.some
        )
        .some
        .map(identity[LoanApproved])
    }

    def enrich = Kleisli[Option, LoanApproved, LoanEnriched] { l =>
      val x = for {
        y <- l.actualRepaymentYears
        s <- l.startDate
      } yield (y, s)
      l.copy(emi = x.map {
          case (y, s) =>
            calculateEMI(y, s)
        })
        .some
        .map(identity[LoanEnriched])
    }

    private def calculateEMI(tenure: Int, startDate: Date): BigDecimal = ???
  }
}
