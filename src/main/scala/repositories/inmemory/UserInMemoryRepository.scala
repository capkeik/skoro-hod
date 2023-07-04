package repositories.inmemory

import cats.Id
import cats.data.{EitherT, OptionT}
import cats.implicits.catsSyntaxEitherId
import domain.users.errors.{LoginInUse, UserError, UserNotFound}
import domain.users.{Login, User, UserId, UserName, UserRepositoryAlgebra, errors}

import scala.collection.mutable
class UserInMemoryRepository extends UserRepositoryAlgebra[Id]{

  private val users = mutable.Set.empty[User]
  override def create(user: User): EitherT[Id, LoginInUse, User] =
    EitherT.pure(findByLogin(user.login).getOrElseF(user).asRight)

  override def update(user: User): EitherT[Id, UserError, User] = {
    if (get(user.id).isEmpty) EitherT.pure(UserNotFound(user.id.value).asLeft)
    else if (!findByLogin(user.login).isEmpty) EitherT.pure(LoginInUse(user.login.value).asLeft)
    else {
      val u = get(user.id).getOrElseF(user)
      users.remove(u)
      users.add(user)
      EitherT.pure(user)
    }
  }

  override def get(userId: UserId): OptionT[Id, User] =
    OptionT(users.find(_.id == userId))

  override def delete(userId: UserId): EitherT[Id, UserNotFound, Unit] =
    if (get(userId).isEmpty) EitherT.pure(UserNotFound(userId.value).asLeft)
    else {
      get(userId).value.map(
        u => {
          users.remove(u)
        }
      )
      EitherT.pure(Unit)
    }

  override def findByUserName(userName: UserName): OptionT[Id, User] =
    OptionT(users.find(_.userName == userName))

  override def list: Id[List[User]] = users.toList

  override def findByLogin(login: Login): OptionT[Id, User] =
    OptionT(users.find(_.login == login))
}
