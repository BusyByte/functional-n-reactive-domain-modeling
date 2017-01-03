package net.nomadicalien.ch5.free.repository.interpreter

import java.util.Date

import net.nomadicalien.ch5.free.free.AccountRepo
import net.nomadicalien.ch5.free.model.Account
import net.nomadicalien.ch5.free.repository._

import scala.collection.mutable.{Map => MMap}
import scalaz._
import Scalaz._
import scalaz.concurrent.Task
import Task._

trait AccountRepoInterpreter {
  def apply[A](action: AccountRepo[A]): Task[A]
}

/**
  * Listing 5.8 Interpreter for AccountRepository (page 176)
  */
case class AccountRepoMutableInterpreter() extends AccountRepoInterpreter {
  val table: MMap[String, Account] = MMap.empty[String, Account]

  val step: AccountRepoF ~> Task = new (AccountRepoF ~> Task) {
    override def apply[A](fa: AccountRepoF[A]): Task[A] = fa match {
      case QueryAccount(no) =>
        table.get(no)
          .map { a => now(a) } // now is a combinator on Task that lifts a strict value into a Task [A].
          .getOrElse {
            fail(new RuntimeException(s"Account no $no not found"))
          }
      case StoreAccount(account) => now(table += ((account.no, account))).void
      case DeleteAccount(no) => now(table -= no).void
    }
  }

  def apply[A](action: AccountRepo[A]): Task[A] = action.foldMap(step)
}



