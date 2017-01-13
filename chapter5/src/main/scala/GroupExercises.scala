package net.nomadicalien.ch5

object GroupExercises {

  trait Functor[F[_]] {
    def map[A, B](as: F[A])(f: A => B): F[B]
  }

  trait Applicative[F[_]] extends Functor[F] {
    def ap[A, B](fa: => F[A])(f: => F[A => B]): F[B]

    def apply2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] =
      ap(fb)(map(fa)(f.curried))

    def lift2[A, B, C](f: (A, B) => C): (F[A], F[B]) => F[C] =
      apply2(_, _)(f)

    def unit[A](a: => A): F[A]
  }

  val optionApplicative = new Applicative[Option] {
    def ap[A, B](fa: => Option[A])(f: => Option[(A) => B]): Option[B] =
      for {
        a  <- fa
        ff <- f
      } yield ff(a)

    def unit[A](a: => A): Option[A] = Some(a)

    def map[A, B](as: Option[A])(f: (A) => B): Option[B] =
      as.map(f)
  }

  trait Monad[F[_]] extends Applicative[F] {
    def flatMap[A, B](ma: F[A])(f: A => F[B]): F[B]
  }

  val monadList = new Monad[List] {
    def flatMap[A, B](ma: List[A])(f: (A) => List[B]): List[B] =
      ma.flatMap(f)

    def ap[A, B](fa: => List[A])(listOfF: => List[(A) => B]): List[B] =
      for {
        a <- fa
        f <- listOfF
      } yield f(a)

    def unit[A](a: => A): List[A] = List(a)

    def map[A, B](as: List[A])(f: (A) => B): List[B] =
      as.map(f)
  }

  val optionList = new Monad[Option] {
    def flatMap[A, B](ma: Option[A])(f: (A) => Option[B]): Option[B] =
      ma.flatMap(f)

    def ap[A, B](fa: => Option[A])(f: => Option[(A) => B]): Option[B] =
      optionApplicative.ap(fa)(f)

    def unit[A](a: => A): Option[A] =
      optionApplicative.unit(a)

    def map[A, B](as: Option[A])(f: (A) => B): Option[B] =
      optionApplicative.map(as)(f)
  }

  import scalaz._
  import Scalaz._

  val runResult = (for {
    _ <- init[String]
    _ <- put("foo")
    f <- modify((s: String) => s + "bar")
  } yield (f)).run("baz")

  import Kleisli._

  trait FooRepository {
    def getFoo(fooId: String): Option[String]
  }

  trait FooService {
    def doubleMyFoo(fooId: String): Kleisli[Option, FooRepository, String] =
      kleisli { fooRepository: FooRepository =>
        fooRepository.getFoo(fooId).map(foo => foo + foo)
      }

    def tripleMyFoo(fooId: String): Kleisli[Option, FooRepository, String] =
      kleisli { fooRepository: FooRepository =>
        fooRepository.getFoo(fooId).map(foo => foo + foo + foo)
      }

    def aWholeLotOfFoo(fooId: String) =
      for {
        d <- doubleMyFoo(fooId)
        t <- tripleMyFoo(fooId)
      } yield (d + t)
  }

  object FooService extends FooService

  object FooRepository extends FooRepository {
    def getFoo(fooId: String): Option[String] = Some("foo")
  }

  val fooRepo: FooRepository = FooRepository
  val fooService: FooService = FooService

  fooService.aWholeLotOfFoo("myId").run(fooRepo)

  object free {
    import scalaz.concurrent.Task
    import scala.collection.mutable
    import scalaz._
    import Scalaz._

    type UserRepo[A] = Free[UserRepositoryF, A]

    case class User(
        firstName: String,
        lastName: String,
        id: Int
    )

    sealed trait UserRepositoryF[+A]

    final case class CreateUser(user: User) extends UserRepositoryF[User]
    final case class GetUser(userId: Int)   extends UserRepositoryF[User]
    final case class UpdateUser(user: User) extends UserRepositoryF[Boolean]

    trait UserRepository {

      def createUser(user: User): UserRepo[User] =
        Free.liftF(CreateUser(user))

      def getUser(userId: Int): UserRepo[User] =
        Free.liftF(GetUser(userId))

      def updateUser(user: User): UserRepo[Boolean] =
        Free.liftF(UpdateUser(user))

      def getAndUpdate(userId: Int, fname: String) =
        for {
          user                <- getUser(userId)
          updatedSuccessfully <- updateUser(user.copy(firstName = fname))
        } yield updatedSuccessfully
    }

    object UserRepository extends UserRepository

    trait UserRepositoryInterpreter {
      def apply[A](action: UserRepo[A]): Task[A]
    }

    case class UserRepositoryMutableInterpreter() extends UserRepository {
      import Task._
      val table: mutable.Map[Int, User] = mutable.Map.empty[Int, User]

      val step: UserRepositoryF ~> Task = new (UserRepositoryF ~> Task) {
        override def apply[A](fa: UserRepositoryF[A]): Task[A] = fa match {
          case CreateUser(user) =>
            now {
              table.update(user.id, user)
              user
            }
          case GetUser(userId) => now(table(userId))
          case UpdateUser(user) =>
            now {
              table.update(user.id, user)
              true
            }
        }
      }

      def apply[A](action: UserRepo[A]): Task[A] = action.foldMap(step)
    }

    val updateUserPlan = for {
      createdUser <- UserRepository.createUser(User("john", "doe", 1234))
      isUpdated   <- UserRepository.getAndUpdate(1234, "bob")
    } yield isUpdated

    val isUpdated = new UserRepositoryMutableInterpreter().apply(updateUserPlan).run
    // scratch pad evaluation prints true
  }

}
