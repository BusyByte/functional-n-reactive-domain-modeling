package net.nomadicalien.ch6

import java.util.{Calendar, Date}

import net.nomadicalien.ch6.Listings.Listing_6_1._
import net.nomadicalien.ch6.Listings.model._
import net.nomadicalien.ch6.Listings.model.common._
import net.nomadicalien.ch6.Listings.repositories.AccountRepositoryInMemory

import scalaz._
import Scalaz._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import scalaz.concurrent.Task

object Listings {

  /**
    * Algebra of the module AccountService (page 184)
    */
  object Listing_6_1 {

    trait AccountRepository {
      def query(no: String): \/[NonEmptyList[String], Option[Account]]
      def store(a: Account): \/[NonEmptyList[String], Account]
      def balance(no: String): \/[NonEmptyList[String], AccountBalance] = query(no) match {
        case \/-(Some(a)) => a.balance.right
        case \/-(None)    => NonEmptyList(s"No account exists with no $no").left[AccountBalance]
        case a @ -\/(_)   => a
      }
      def query(openedOn: Date): \/[NonEmptyList[String], Seq[Account]]
      def all: \/[NonEmptyList[String], Seq[Account]]
    }

    sealed trait AccountType
    case object Checking extends AccountType
    case object Savings  extends AccountType

    trait AccountService[Account, Amount, Balance] {
      type Valid[A]            = EitherT[Future, NonEmptyList[String], A]
      type AccountOperation[A] = Kleisli[Valid, AccountRepository, A]

      def open(no: String,
               name: String,
               rate: Option[BigDecimal],
               openingDate: Option[Date],
               accountType: AccountType): AccountOperation[Account]

      def close(no: String, closeDate: Option[Date]): AccountOperation[Account]
      def debit(no: String, amount: Amount): AccountOperation[Account]
      def credit(no: String, amount: Amount): AccountOperation[Account]
      def balance(no: String): AccountOperation[Balance]
      def transfer(from: String, to: String, amount: Amount): AccountOperation[(Account, Account)]
    }
  }

  /**
    * Interpreter for AccountService (page 187)
    */
  object Listing_6_2 {
    import Kleisli._

    class AccountServiceInterpreter extends AccountService[Account, Amount, AccountBalance] {
      def open(no: String,
               name: String,
               rate: Option[BigDecimal],
               openingDate: Option[Date],
               accountType: AccountType) =
        kleisli[Valid, AccountRepository, Account] { (repo: AccountRepository) =>
          EitherT {
            Future {
              repo.query(no) match {
                case \/-(Some(a)) => NonEmptyList(s"Already existing account with no $no").left[Account]
                case \/-(None) =>
                  accountType match {
                    case Checking =>
                      Account.checkingAccount(no, name, openingDate, None, AccountBalance()).flatMap(repo.store)
                    case Savings =>
                      rate map { r =>
                        Account.savingsAccount(no, name, r, openingDate, None, AccountBalance()).flatMap(repo.store)
                      } getOrElse {
                        NonEmptyList(s"Rate needs to be given for savings account").left[Account]
                      }
                  }
                case a @ -\/(_) => a
              }
            }
          }
        }

      def close(no: String, closeDate: Option[Date]): AccountOperation[Account] = ???

      def debit(no: String, amount: Amount): AccountOperation[Account] = ???

      def credit(no: String, amount: Amount): AccountOperation[Account] = ???

      def balance(no: String): AccountOperation[AccountBalance] = ???

      def transfer(from: String, to: String, amount: Amount): AccountOperation[(Account, Account)] = ???
    }

  }

  object model {

    case class AccountBalance(amount: Amount = 0)

    // Listing 6.5 Account domain model page 191
    sealed trait Account {
      def no: String
      def name: String
      def dateOfOpen: Option[Date]
      def dateOfClose: Option[Date]
      def balance: AccountBalance
    }

    final case class CheckingAccount(no: String,
                                     name: String,
                                     dateOfOpen: Option[Date],
                                     dateOfClose: Option[Date] = None,
                                     balance: AccountBalance = AccountBalance())
        extends Account

    final case class SavingsAccount(no: String,
                                    name: String,
                                    rateOfInterest: Amount,
                                    dateOfOpen: Option[Date],
                                    dateOfClose: Option[Date] = None,
                                    balance: AccountBalance = AccountBalance())
        extends Account

    object Account {
      private def validateAccountNo(no: String) =
        if (no.isEmpty || no.size < 5) s"Account No has to be at least 5 characters long: found $no".failureNel[String]
        else no.successNel[String]

      private def validateOpenCloseDate(od: Date, cd: Option[Date]) =
        cd.map { c =>
            if (c before od)
              s"Close date [$c] cannot be earlier than open date [$od]".failureNel[(Option[Date], Option[Date])]
            else (od.some, cd).successNel[String]
          }
          .getOrElse { (od.some, cd).successNel[String] }

      private def validateRate(rate: BigDecimal) =
        if (rate <= BigDecimal(0)) s"Interest rate $rate must be > 0".failureNel[BigDecimal]
        else rate.successNel[String]

      def checkingAccount(no: String,
                          name: String,
                          openDate: Option[Date],
                          closeDate: Option[Date],
                          balance: AccountBalance): \/[NonEmptyList[String], Account] = {

        val od = openDate.getOrElse(today)

        (
          validateAccountNo(no) |@|
            validateOpenCloseDate(openDate.getOrElse(today), closeDate)
        ) { (n, d) =>
          CheckingAccount(n, name, d._1, d._2, balance)
        }.disjunction
      }

      def savingsAccount(no: String,
                         name: String,
                         rate: BigDecimal,
                         openDate: Option[Date],
                         closeDate: Option[Date],
                         balance: AccountBalance): \/[NonEmptyList[String], Account] = {

        val od = openDate.getOrElse(today)

        (
          validateAccountNo(no) |@|
            validateOpenCloseDate(openDate.getOrElse(today), closeDate) |@|
            validateRate(rate)
        ) { (n, d, r) =>
          SavingsAccount(n, name, r, d._1, d._2, balance)
        }.disjunction
      }

      private def validateAccountAlreadyClosed(a: Account) = {
        if (a.dateOfClose isDefined) s"Account ${a.no} is already closed".failureNel[Account]
        else a.successNel[String]
      }

      private def validateCloseDate(a: Account, cd: Date) = {
        if (cd before a.dateOfOpen.get)
          s"Close date [$cd] cannot be earlier than open date [${a.dateOfOpen.get}]".failureNel[Date]
        else cd.successNel[String]
      }

      def close(a: Account, closeDate: Date): \/[NonEmptyList[String], Account] = {
        (validateAccountAlreadyClosed(a) |@| validateCloseDate(a, closeDate)) { (acc, d) =>
          acc match {
            case c: CheckingAccount => c.copy(dateOfClose = Some(closeDate))
            case s: SavingsAccount  => s.copy(dateOfClose = Some(closeDate))
          }
        }.disjunction
      }

      private def checkBalance(a: Account, amount: Amount) = {
        if (amount < 0 && a.balance.amount < -amount) s"Insufficient amount in ${a.no} to debit".failureNel[Account]
        else a.successNel[String]
      }

      def updateBalance(a: Account, amount: Amount): \/[NonEmptyList[String], Account] = {
        (validateAccountAlreadyClosed(a) |@| checkBalance(a, amount)) { (_, _) =>
          a match {
            case c: CheckingAccount => c.copy(balance = AccountBalance(c.balance.amount + amount))
            case s: SavingsAccount  => s.copy(balance = AccountBalance(s.balance.amount + amount))
          }
        }.disjunction
      }

      def rate(a: Account) = a match {
        case SavingsAccount(_, _, r, _, _, _) => r.some
        case _                                => None
      }
    }

    object common {
      type Amount = BigDecimal
      def today = Calendar.getInstance.getTime
    }

    // Listing_6_3 The Portfolio domain model (page 190)
    case class PortfolioBalance(ins: Instrument, holding: Amount, marketValue: Amount)

    sealed trait Portfolio {
      def accountNo: String

      def asOf: Date

      def items: Seq[PortfolioBalance]

      def totalMarketValue: Amount =
        items.foldLeft(BigDecimal(0d)) { (acc, i) =>
          acc + i.marketValue
        }
    }

    case class CustomerPortfolio(accountNo: String, asOf: Date, items: Seq[PortfolioBalance]) extends Portfolio

    // Listing 6.4 The Instrument domain model (page 191)
    sealed trait InstrumentType

    case object CCY extends InstrumentType

    case object EQ extends InstrumentType

    case object FI extends InstrumentType

    sealed trait Instrument {
      def instrumentType: InstrumentType
    }

    case class Equity(isin: String, name: String, issueDate: Date, faceValue: Amount) extends Instrument {
      final val instrumentType = EQ
    }

    case class FixedIncome(isin: String, name: String, issueDate: Date, maturityDate: Option[Date], nominal: Amount)
        extends Instrument {
      final val instrumentType = FI
    }

    case class Currency(isin: String) extends Instrument {
      final val instrumentType = CCY

    }

  }

  /**
    * Portfolio reporting service (page 192)
    */
  object Listing_6_6 {
    trait PortfolioService {
      type PFOperation[A] = Kleisli[Future, AccountRepository, Seq[A]]
      def getCurrencyPortfolio(no: String, asOf: Date): PFOperation[PortfolioBalance]
      def getEquityPortfolio(no: String, asOf: Date): PFOperation[PortfolioBalance]
      def getFixedIncomePortfolio(no: String, asOf: Date): PFOperation[PortfolioBalance]
    }

    trait PortfolioServiceInterpreter extends PortfolioService {
      def getCurrencyPortfolio(no: String, asOf: Date): PFOperation[PortfolioBalance]    = ???
      def getEquityPortfolio(no: String, asOf: Date): PFOperation[PortfolioBalance]      = ???
      def getFixedIncomePortfolio(no: String, asOf: Date): PFOperation[PortfolioBalance] = ???
    }

    object PortfolioService extends PortfolioServiceInterpreter
  }

  object repositories {
    object AccountRepositoryInMemory extends AccountRepository {
      def query(no: String): Disjunction[NonEmptyList[String], Option[Account]]  = ???
      def store(a: Account): Disjunction[NonEmptyList[String], Account]          = ???
      def query(openedOn: Date): Disjunction[NonEmptyList[String], Seq[Account]] = ???
      def all: Disjunction[NonEmptyList[String], Seq[Account]]                   = ???
    }
  }

  /**
    * Building the complete portfolio (page 193)
    */
  object Listing_6_7 {
    import net.nomadicalien.ch6.Listings.Listing_6_6.PortfolioService._
    val accountNo: String = "123"
    val asOf              = today

    val ccyPF: Future[Seq[PortfolioBalance]] =
      getCurrencyPortfolio(accountNo, asOf)(AccountRepositoryInMemory)
    val eqtPF: Future[Seq[PortfolioBalance]] =
      getEquityPortfolio(accountNo, asOf)(AccountRepositoryInMemory)
    val fixPF: Future[Seq[PortfolioBalance]] =
      getFixedIncomePortfolio(accountNo, asOf)(AccountRepositoryInMemory)

    val portfolio: Future[Portfolio] = for {
      c <- ccyPF
      e <- eqtPF
      f <- fixPF
    } yield CustomerPortfolio(accountNo, asOf, c ++ e ++ f)
  }

  /**
    *  Using Task for PortfolioService (page 194)
    */
  object Listing_6_8 {
    trait PortfolioService {
      type PFOperation[A] = Kleisli[Task, AccountRepository, Seq[A]]
      def getCurrencyPortfolio(no: String, asOf: Date): PFOperation[PortfolioBalance]
      def getEquityPortfolio(no: String, asOf: Date): PFOperation[PortfolioBalance]
      def getFixedIncomePortfolio(no: String, asOf: Date): PFOperation[PortfolioBalance]
    }

    trait PortfolioServiceInterpreter extends PortfolioService {
      def getCurrencyPortfolio(no: String, asOf: Date): PFOperation[PortfolioBalance]    = ???
      def getEquityPortfolio(no: String, asOf: Date): PFOperation[PortfolioBalance]      = ???
      def getFixedIncomePortfolio(no: String, asOf: Date): PFOperation[PortfolioBalance] = ???
    }

    object PortfolioService extends PortfolioServiceInterpreter
    import PortfolioService._

    val accountNo = "a-123"
    val asOf      = today
    val ccyPF: Task[Seq[PortfolioBalance]] =
      getCurrencyPortfolio(accountNo, asOf)(AccountRepositoryInMemory)
    val eqtPF: Task[Seq[PortfolioBalance]] =
      getEquityPortfolio(accountNo, asOf)(AccountRepositoryInMemory)
    val fixPF: Task[Seq[PortfolioBalance]] =
      getFixedIncomePortfolio(accountNo, asOf)(AccountRepositoryInMemory)
    val r = Task.gatherUnordered(Seq(ccyPF, eqtPF, fixPF))
    val portfolio =
      CustomerPortfolio(accountNo, asOf, r.unsafePerformSync.foldLeft(List.empty[PortfolioBalance])(_ ++ _))

  }

}
