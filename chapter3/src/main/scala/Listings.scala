package net.nomadicalien.ch3

import java.util.{Calendar, Date}

import net.nomadicalien.ch3.Listing_3_1.AccountService

import scala.util.{Failure, Success, Try}


/**
  *  The AccountService module with supported operations
  */
object Listing_3_1 {
  trait AccountService[Account, Amount, Balance] { // Parameterized on types
    def open(no: String, name: String, openDate: Option[Date]): Try[Account]
    def close(account: Account, closeDate: Option[Date]): Try[Account]
    def debit(account: Account, amount: Amount): Try[Account]
    def credit(account: Account, amount: Amount): Try[Account]
    def balance(account: Account): Try[Balance]
  }
}

/**
  * Base abstractions for your AccountService API implementation
  */
object Listing_3_2 {
  type Amount = BigDecimal
  def today = Calendar.getInstance.getTime
  case class Balance(amount: Amount = 0)
  case class Account(no: String, name: String, dateOfOpen: Date,
    dateOfClose: Option[Date] = None, balance: Balance = Balance(0))
}

/**
  * The interpreter of your algebra
  */
object Listing_3_3 {
  import Listing_3_2._
  object AccountService extends AccountService[Account, Amount, Balance] {
    def open(no: String, name: String, openingDate: Option[Date]) : Try[Account] = {
      if (no.isEmpty || name.isEmpty)
        Failure(new Exception(s"Account no or name cannot be blank") )
      else if (openingDate.getOrElse(today) before today)
        Failure(new Exception(s"Cannot open account in the past"))
      else Success(Account(no, name, openingDate.getOrElse(today)))
    }
    def close(account: Account, closeDate: Option[Date]): Try[Account] = {
      val cd = closeDate.getOrElse(today)
      if (cd before account.dateOfOpen)
        Failure(new Exception(s"Close date $cd cannot be before ➥ opening date ${account.dateOfOpen}"))
      else Success(account.copy(dateOfClose = Some(cd)))
    }
    def debit(a: Account, amount: Amount): Try[Account] = {
      if (a.balance.amount < amount)
        Failure(new Exception("Insufficient balance"))
      else Success(a.copy(balance = Balance(a.balance.amount - amount)))
    }
    def credit(a: Account, amount: Amount): Try[Account] =
      Success(a.copy(balance = Balance(a.balance.amount + amount)))
    def balance(account: Account): Try[Balance] = Success(account.balance)
  }
}


/**
  *  A DayOfWeek using the smart constructor idiom
  */
object Listing_3_4 {
  sealed trait DayOfWeek { // The core abstraction that models a day of the week
    val value: Int
    override def toString = value match {
      case 1 => "Monday"
      case 2 => "Tuesday"
      case 3 => "Wednesday"
      case 4 => "Thursday"
      case 5 => "Friday"
      case 6 => "Saturday"
      case 7 => "Sunday"
    }
  }


  object DayOfWeek { // The companion object or the module
    private def unsafeDayOfWeek(d: Int) = new DayOfWeek { val value = d }
    private val isValid: Int => Boolean = { i => i >= 1 && i <= 7 }
    def dayOfWeek(d: Int): Option[DayOfWeek] = if (isValid(d)) // The published smart constructor
      Some(unsafeDayOfWeek(d)) else None
  }
}