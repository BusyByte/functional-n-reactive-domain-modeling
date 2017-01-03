package net.nomadicalien.ch5.free.repository

import java.util.Date

import net.nomadicalien.ch5.free.free.AccountRepo
import net.nomadicalien.ch5.free.model.common.Amount
import net.nomadicalien.ch5.free.model.{Account, Balance}

import scalaz.Free


// Listing 5.6 The building blocks of account repository DSL
sealed trait AccountRepoF[+A]
case class QueryAccount(no: String) extends AccountRepoF[Account]
case class StoreAccount(account: Account) extends AccountRepoF[Unit]
case class DeleteAccount(no: String) extends AccountRepoF[Unit]


/**
  * Listing 5.4 The AccountRepository module definition (page 169)
  */
trait AccountRepository {

  // liftF lifts the Store operation into the context of the free monad AccountRepo.
  def store(a: Account): AccountRepo[Unit] = Free.liftF(StoreAccount(a))
  def query(no: String): AccountRepo[Account]= Free.liftF(QueryAccount(no))
  def delete(no: String): AccountRepo[Unit] =
    Free.liftF(DeleteAccount(no))
  def update(no: String, f: Account => Account): AccountRepo[Unit] = for {
    a <- query(no)
    _ <- store(f(a))
  } yield ()

  def updateBalance(no: String, amount: Amount,
    f: (Account, Amount) => Account): AccountRepo[Unit] = for {
    a <- query(no)
    _ <- store(f(a, amount))
  } yield ()
}
