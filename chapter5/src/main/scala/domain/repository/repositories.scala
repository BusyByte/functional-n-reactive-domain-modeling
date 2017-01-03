package net.nomadicalien.ch5.domain.repository

import java.util.Date

import net.nomadicalien.ch5.domain.model.{Account, Balance}

import scalaz._
import Scalaz._

trait AccountRepository {
  def query(no: String): \/[NonEmptyList[String], Option[Account]]
  def store(a: Account): \/[NonEmptyList[String], Account]
  def balance(no: String): \/[NonEmptyList[String], Balance] = query(no) match {
    case \/-(Some(a)) => a.balance.right
    case \/-(None)    => NonEmptyList(s"No account exists with no $no").left[Balance]
    case a @ -\/(_)   => a
  }
  def query(openedOn: Date): \/[NonEmptyList[String], Seq[Account]]
  def all: \/[NonEmptyList[String], Seq[Account]]
}
