package net.nomadicalien.ch1

import java.util.Date

/**
  * Account as an aggregate
  */
object Listing1_2 {

  final case class Address(line1: String, line2: String, city: String, state: String, zip: String)

  sealed trait Bank

  /**
    * Contract for account aggregate
    */
  trait Account {
    def no: String
    def name: String
    def bank: Bank       // reference to another Entity
    def address: Address // value object
    def dateOfOpening: Date
    def dateOfClose: Option[Date]

    //..
  }

  type Amount = BigDecimal
  type Rate   = BigDecimal

  /**
    * A concrete implementation of Account. Note that the fields override the defs of the trait.
    */
  case class CheckingAccount(
      no: String,
      name: String,
      bank: Bank,
      address: Address,
      dateOfOpening: Date,
      dateOfClose: Option[Date]
      //..
  ) extends Account

  case class SavingsAccount(
      no: String,
      name: String,
      bank: Bank,
      address: Address,
      dateOfOpening: Date,
      dateOfClose: Option[Date],
      rateOfInterest: Rate
      //..
  ) extends Account

  /**
    * Domain Service
    */
  trait AccountService {
    def transfer(from: Account, to: Account, amount: Amount): Option[Amount]
  }

}
