package net.nomadicalien

/**
  * Factory for instantiating accounts in Scala
  */
object Listing1_1 {
  sealed trait AccountHolder {
    def name: String
  }

  final case class Person(name: String)   extends AccountHolder
  final case class Business(name: String) extends AccountHolder

  sealed trait Account {
    def number: Int
    def holder: AccountHolder

  }
  final case class CheckingAccount(number: Int, holder: AccountHolder)    extends Account
  final case class SavingsAccount(number: Int, holder: AccountHolder)     extends Account
  final case class MoneyMarketAccount(number: Int, holder: AccountHolder) extends Account

  object Account {

    /**
      * Factory method that instantiates Accounts
      */
    def apply(accountType: String, number: Int, holder: AccountHolder): Option[Account] = accountType match {
      case "C" => Some(CheckingAccount(number, holder))
      case "S" => Some(SavingsAccount(number, holder))
      case "M" => Some(MoneyMarketAccount(number, holder))
      case _   => None
    }
  }
}
