package domain.users

import cats.data.{EitherT, OptionT}
import domain.users.errors.{LoginInUse, UserError, UserNotFound}

import java.util.UUID

trait UserRepositoryAlgebra[F[_]] {
  def create(user: User): EitherT[F, LoginInUse, User]

  def update(user: User): EitherT[F, UserError, User]

  def get(userId: UserId): OptionT[F, User]

  def delete(userId: UserId): EitherT[F, UserNotFound, Unit]

  def findByUserName(userName: UserName): OptionT[F, User]

  def deleteByUserName(userName: UserName): EitherT[F, UserNotFound, Unit]

  def list: F[List[User]]
}
