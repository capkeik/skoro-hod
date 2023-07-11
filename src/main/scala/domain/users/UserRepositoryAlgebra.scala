package domain.users

import cats.data.{EitherT, OptionT}
import domain.AppError
import domain.users.errors.{UserError, UserIdNotFound}

trait UserRepositoryAlgebra[F[_]] {
  def create(user: CreateUser): EitherT[F, AppError, User]

  def update(user: User): EitherT[F, UserError, User]

  def get(userId: UserId): F[Either[AppError, Option[User]]]

  def delete(userId: UserId): EitherT[F, UserIdNotFound, Unit]

  def findByLogin(login: Login): F[Either[AppError, Option[User]]]

  def list: F[List[User]]
}
