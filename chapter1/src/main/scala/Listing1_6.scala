package net.nomadicalien.ch1

/**
  * Purifying the model
  */
object Listing1_6 {

  import java.util.{Date, Calendar}
  import scala.util.{Try, Success, Failure}

  def today = Calendar.getInstance.getTime
  type Amount = BigDecimal

  case class Balance(amount: Amount = 0)

  //Account aggregate is now an ADT
  case class Account(no: String, name: String, dateOfOpening: Date, balance: Balance = Balance())

  //---State (above) and behavior (below) decoupled -----

  /**
    *  Domain service with the operations debit and credit
    */
  trait AccountService {

    def debit(a: Account, amount: Amount): Try[Account] = {
      if (a.balance.amount < amount)
        Failure(new Exception("Insufficient balance in account"))
      else Success(a.copy(balance = Balance(a.balance.amount - amount)))
    }
    def credit(a: Account, amount: Amount): Try[Account] =
      Success(a.copy(balance = Balance(a.balance.amount + amount)))
  }

  /**
    * Concrete instance of the service using the object keyword
    */
  object AccountService extends AccountService

  //Pure function invocation: returns a Try[Account]
  import AccountService._
  val a = Account("a1", "John", today)
  a.balance == Balance(0)
  val b = credit(a, 1000)
}
