package net.nomadicalien.ch5.domain.service

import java.util.Date

import net.nomadicalien.ch5.domain.repository.AccountRepository

import scalaz.{Kleisli, NonEmptyList, \/}

// Listing_5_1 (page 154)

sealed trait AccountType
case object Checking extends AccountType
case object Savings  extends AccountType

/**
  * Algebra of the module AccountService
  */
trait AccountService[Account, Amount, Balance] {
  type Valid[A]            = NonEmptyList[String] \/ A
  type AccountOperation[A] = Kleisli[Valid, AccountRepository, A]

  /**
    *  Operation definition with domain-friendly name
    */
  def open(no: String,
           name: String,
           rate: Option[BigDecimal],
           openingDate: Option[Date],
           accountType: AccountType): AccountOperation[Account]

  def close(no: String, closeDate: Option[Date]): AccountOperation[Account]
  def debit(no: String, amount: Amount): AccountOperation[Account]

  def credit(no: String, amount: Amount): AccountOperation[Account]
  def balance(no: String): AccountOperation[Balance]
  def transfer(from: String, to: String, amount: Amount): AccountOperation[(Account, Account)] =
    for {
      a <- debit(from, amount)
      b <- credit(to, amount)
    } yield ((a, b))
}

trait InterestCalculation[Account, Amount] {
  def computeInterest: Kleisli[Valid, Account, Amount]
}

trait TaxCalculation[Amount] {
  def computeTax: Kleisli[Valid, Amount, Amount]
}

trait InterestPostingService[Account, Amount] extends InterestCalculation[Account, Amount] with TaxCalculation[Amount]
