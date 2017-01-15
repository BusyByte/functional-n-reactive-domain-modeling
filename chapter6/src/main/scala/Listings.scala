package net.nomadicalien.ch6

import java.util.Date

import net.nomadicalien.ch6.Listings.Listing_6_1.{AccountRepository, AccountService, AccountType}

import scalaz._
import Scalaz._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

object Listings {

  /**
    * Algebra of the module AccountService (page 184)
    */
  object Listing_6_1 {

    trait AccountRepository
    trait AccountType

    trait AccountService[Account, Amount, Balance] {
      type Valid[A] = NonEmptyList[String] \/ A
      type AccountOperation[A] = Kleisli[Valid, AccountRepository, A]

      def open(no: String, name: String, rate: Option[BigDecimal],
        openingDate: Option[Date], accountType: AccountType):
      AccountOperation[Account]

      def close(no: String, closeDate: Option[Date]): AccountOperation[Account]
      def debit(no: String, amount: Amount): AccountOperation[Account]
      def credit(no: String, amount: Amount): AccountOperation[Account]
      def balance(no: String): AccountOperation[Balance]
      def transfer(from: String, to: String, amount: Amount): AccountOperation[(Account, Account)]
    }
  }

  /**
    * Interpreter for AccountService (page 187)
    */
  object Listing_6_2 {
    import Kleisli._
    sealed trait Account
    case class SavingsAccount() extends Account

    trait Amount

    trait Balance
    //package domain
    //package service
    //package interpreter
    //Imports as in listing 5.2
    class AccountServiceInterpreter extends
      AccountService[Account, Amount, Balance] {
      def open(no: String,
        name: String,
        rate: Option[BigDecimal],
        openingDate: Option[Date],
        accountType: AccountType) =
        kleisli[Valid, AccountRepository, Account] {
          (repo: AccountRepository) =>
            EitherT {
              Future {
                SavingsAccount()
              }
            }
        }
          //.. other operations
        }

  }
}
