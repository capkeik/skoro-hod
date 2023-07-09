package domain.users

import cats.data.{EitherT, OptionT}
import domain.users.errors.{LoginInUse, UserError, UserIdNotFound}

trait UserRepositoryAlgebra[F[_]] {
  def create(user: CreateUser): EitherT[F, LoginInUse, User]

  def update(user: User): EitherT[F, UserError, User]

  def get(userId: UserId): OptionT[F, User]

  def delete(userId: UserId): EitherT[F, UserIdNotFound, Unit]

  def findByLogin(login: Login): OptionT[F, User]

  def list: F[List[User]]
}
