package net.nomadicalien.ch2

import java.util.{Calendar, Date}

/**
  * Scala case classes, which are immutable by default
  */
object Listing2_10 {
  sealed trait Account

  case class SavingsAccount(number: String, name: String, dateOfOpening: Date, rateOfInterest: BigDecimal)
      extends Account
  val today = Calendar.getInstance.getTime

  //Original account
  val a1 = SavingsAccount("a-123", "google", today, 0.2)

  //Copy with the changed value
  val a2 = a1.copy(rateOfInterest = 0.15)

}
