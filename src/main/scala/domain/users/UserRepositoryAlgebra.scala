package domain.users

import cats.data.{EitherT, OptionT}
import domain.users.errors.{LoginInUse, UserError}

import java.util.UUID

trait UserRepositoryAlgebra[F[_]] {
  def create(user: User): EitherT[F, LoginInUse, User]

  def update(user: User): EitherT[F, UserError, User]

  def get(userId: UUID): OptionT[F, User]

  def delete(userId: UUID): OptionT[F, User]

  def findByUserName(userName: String): OptionT[F, User]

  def deleteByUserName(userName: String): OptionT[F, User]

  def list: F[List[User]]
}
