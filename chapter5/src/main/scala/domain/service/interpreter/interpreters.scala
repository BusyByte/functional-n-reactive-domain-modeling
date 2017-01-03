package net.nomadicalien.ch5.domain.service.interpreter


import java.util.{Calendar, Date}
import java.util.Date

import net.nomadicalien.ch5.domain.model.{Account, Balance}
import net.nomadicalien.ch5.domain.model.common._
import net.nomadicalien.ch5.domain.repository.AccountRepository
import net.nomadicalien.ch5.domain.service.{AccountService, AccountType, Checking, InterestPostingService, Savings, Valid}

import scalaz._
import Scalaz._
import \/._
import Kleisli._

// Listing 5.2 Interpreter for AccountService (page 157)
class AccountServiceInterpreter extends AccountService[Account, Amount, Balance] {

  def open(no: String,
    name: String,
    rate: Option[BigDecimal],
    openingDate: Option[Date],
    accountType: AccountType) = kleisli { (repo: AccountRepository) =>
    repo.query(no) match {
      case \/-(Some(a)) =>
        NonEmptyList(s"Already existing account with no $no").left[Account]
      case \/-(None) => accountType match {
        case Checking =>
          Account.checkingAccount(no, name, openingDate, None,
            Balance()).flatMap(repo.store)
        case Savings => rate map { r =>
          Account.savingsAccount(no, name, r, openingDate, None,
            Balance()).flatMap(repo.store)
        } getOrElse {
          NonEmptyList(s"Rate needs to be given for savings account").left[Account]
        }
      }
      case a @ -\/(_) => a
    }
  }

  def close(no: String, closeDate: Option[Date]) =
    kleisli { (repo: AccountRepository) =>
      repo.query(no) match {
        case \/-(None) => NonEmptyList(s"Account $no does not ➥ exist").left[Account]
        case \/-(Some(a)) =>
          val cd = closeDate.getOrElse(today)
          Account.close(a, cd).flatMap(repo.store)
        case a @ -\/(_) => a
      }
    }

  //.. other operations
  def debit(no: String, amount: Amount): AccountOperation[Account] = ???
  def credit(no: String, amount: Amount): AccountOperation[Account] = ???
  def balance(no: String): AccountOperation[Balance] = ???
}

object AccountService extends AccountServiceInterpreter


class InterestPostingServiceInterpreter extends InterestPostingService[Account, Amount] {
  def computeInterest = kleisli[Valid, Account, Amount] { (account: Account) =>
    if (account.dateOfClose isDefined) NonEmptyList(s"Account ${account.no} is closed").left
    else Account.rate(account).map { r =>
      val a = account.balance.amount
      a + a * r
    }.getOrElse(BigDecimal(0)).right
  }

  def computeTax = kleisli[Valid, Amount, Amount] { amount: Amount =>
    (amount * 0.1).right
  }
}

object InterestPostingService extends InterestPostingServiceInterpreter