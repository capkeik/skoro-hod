package repositories.inmemory

import cats.Id
import cats.data.{EitherT, OptionT}
import cats.effect.IO
import cats.implicits.catsSyntaxEitherId
import domain.users.Role.Admin
import domain.users.errors.{LoginInUse, UserError, UserIdNotFound}
import domain.users.{Login, Password, User, UserId, UserName, UserRepositoryAlgebra}

import java.util.UUID
import scala.collection.mutable

class UserInMemoryRepository extends UserRepositoryAlgebra[IO] {

  private val users = mutable.Set(
    User(
      UserName("test user"),
      Login("admin"),
      Password("admin"),
      UserId(UUID.randomUUID()),
      Admin
    )
  )

  override def create(user: User): EitherT[IO, LoginInUse, User] =
    EitherT.fromEither(
      if (users.add(user)) {
        user.asRight
    } else LoginInUse(user.login.value).asLeft[User])

  override def findByLogin(login: Login): OptionT[IO, User] =
    OptionT[IO, User](IO.pure(users.find(_.login == login)))

  override def update(user: User): EitherT[IO, UserError, User] = ???

  override def delete(userId: UserId): EitherT[IO, UserIdNotFound, Unit] = ???


  override def get(userId: UserId): OptionT[IO, User] =
    OptionT[IO, User](IO.pure(users.find(_.id == userId)))

  override def list: IO[List[User]] = IO.pure(users.toList)
}
object UserInMemoryRepository {
  def make = new UserInMemoryRepository
}
