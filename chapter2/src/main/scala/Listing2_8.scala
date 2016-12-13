package net.nomadicalien.ch2

import java.util.Date

/**
  * The Account abstraction (product type)
  */
object Listing2_8 {
  sealed trait Account {
    def number: String
    def name: String
  }
  case class CheckingAccount(number: String, name: String, dateOfOpening: Date) extends Account
  case class SavingsAccount(number: String, name: String, dateOfOpening: Date, rateOfInterest: BigDecimal)
      extends Account
}
