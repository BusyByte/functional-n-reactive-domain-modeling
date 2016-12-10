package net.nomadicalien.ch2

import net.nomadicalien.ch2.Listing2_1.{DateRange, InterestRate}

import scala.util.Try

/**
  * Calculating interest—a polymorphic version
  */
object Listing2_3 {

  /**
    * Base abstractions and a sample calculateInterest function
    */
  sealed trait Account {
    def number: String
    def name: String
    //May contain other attributes
  }

  // Separate types for checking and savings modeled as algebraic data types
  final case class CheckingAccount(number: String, name: String) extends Account

  sealed trait InterestBearingAccount extends Account {
    def rateOfInterest: InterestRate
  }
  final case class SavingsAccount(number: String, name: String) extends InterestBearingAccount {
    def rateOfInterest = 1.0
  }

  final case class MoneyMarketAccount(number: String, name: String) extends InterestBearingAccount {
    def rateOfInterest = 2.0
  }

  trait AccountService {
    def calculateInterest[A <: InterestBearingAccount](account: A, period: DateRange): Try[BigDecimal] = ???
  }

  object AccountService extends AccountService

}
