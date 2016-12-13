package net.nomadicalien.ch2

/**
  * Modelling currency (sum type)
  */
object Listing2_6 {
  sealed trait Currency
  case object USD extends Currency
  case object AUD extends Currency
  case object EUR extends Currency
  case object INR extends Currency
}
