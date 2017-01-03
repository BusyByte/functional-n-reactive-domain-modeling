package net.nomadicalien.ch5.domain.application

import net.nomadicalien.ch5.domain.model.Account
import net.nomadicalien.ch5.domain.model.common.Amount
import net.nomadicalien.ch5.domain.repository.AccountRepository
import net.nomadicalien.ch5.domain.repository.interpreter.AccountRepositoryInMemory
import net.nomadicalien.ch5.domain.service.Savings
import net.nomadicalien.ch5.domain.service.interpreter


/**
  * Using the modules in the client application Listing 5.3 (page 162)
  */
object App extends App {
  import scalaz._
  import Scalaz._
  import interpreter.AccountService._
  import interpreter.InterestPostingService._

  def postTransactions(a: Account, creditAmount: Amount, debitAmount: Amount):
  Kleisli[Valid, AccountRepository, Account] = for {
    _ <- credit(a.no, creditAmount)
    d <- debit(a.no, debitAmount)
  } yield d

  def composite(no: String, name: String, creditAmount: Amount, debitAmount:
    Amount): Kleisli[Valid, AccountRepository, Amount] = (for {
    a <- open(no, name, BigDecimal(0.4).some, None, Savings)
    t <- postTransactions(a, creditAmount, debitAmount)
  } yield t) andThen computeInterest andThen computeTax

  val x = composite("a-123", "John k", 10000, 2000)
  (AccountRepositoryInMemory)
}
