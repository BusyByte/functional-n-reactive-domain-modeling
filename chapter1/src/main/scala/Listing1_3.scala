package net.nomadicalien.ch1

object Listing1_3 {
  import Listing1_2._

  sealed trait Criteria[T] //not sure why this needs to be a generic?

  trait AccountRepository {
    def query(accountNo: String): Option[Account]
    def query(criteria: Criteria[Account]): Seq[Account]
    def write(accounts: Seq[Account]): Boolean
    def delete(account: Account): Boolean
  }
}
