package net.nomadicalien.ch5.domain.repository

import net.nomadicalien.ch5.domain.model.Account

import scalaz.{NonEmptyList, \/}

sealed trait AccountRepository {
  def query(no: String): \/[NonEmptyList[String], Option[Account]]
  def store(a: Account): \/[NonEmptyList[String], Account]
}