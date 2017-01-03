package net.nomadicalien.ch5.domain.repository.interpreter

import java.util.Date

import net.nomadicalien.ch5.domain.model.Account
import net.nomadicalien.ch5.domain.repository.AccountRepository

import scala.collection.mutable.{Map => MMap}
import scalaz._
import Scalaz._

trait AccountRepositoryInMemory extends AccountRepository {
  lazy val repo = MMap.empty[String, Account]

  def query(no: String): \/[NonEmptyList[String], Option[Account]] = repo.get(no).right
  def store(a: Account): \/[NonEmptyList[String], Account] = {
    val r = repo += ((a.no, a))
    a.right
  }
  def query(openedOn: Date): \/[NonEmptyList[String], Seq[Account]] =
    repo.values.filter(_.dateOfOpen == openedOn).toSeq.right
  def all: \/[NonEmptyList[String], Seq[Account]] = repo.values.toSeq.right
}

object AccountRepositoryInMemory extends AccountRepositoryInMemory
