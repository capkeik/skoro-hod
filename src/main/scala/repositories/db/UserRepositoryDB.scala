package repositories.db

import cats.data.{EitherT, OptionT}
import cats.effect.kernel.MonadCancelThrow
import cats.implicits._
import domain.{AppError, DBError, users}
import domain.users.errors.{LoginInUse, UserError, UserIdNotFound}
import domain.users.{CreateUser, User, UserRepositoryAlgebra, errors}
import doobie._
import doobie.implicits._
import repositories.db.dao.UserSql

object UserRepositoryDB {
  final class Impl[F[_]: MonadCancelThrow](
    sql: UserSql,
    transactor: Transactor[F]
  ) extends UserRepositoryAlgebra[F] {
    override def create(user: CreateUser): EitherT[F, AppError, User] =
      EitherT(
        sql.create(user).transact(transactor).attempt.map {
          case Left(th) => DBError("Something went wrong in DB: ", Some(th)).asLeft[User]
          case Right(Left(e)) => e.asLeft[User]
          case Right(Right(u)) => u.asRight[AppError]
        }
      )
    override def update(user: User): EitherT[F, UserError, User] = ???

    override def get(userId: users.UserId): F[Either[AppError, Option[User]]] =
      sql.get(userId).transact(transactor).attempt.map {
        case Right(op) => op.asRight[DBError]
        case Left(th) => DBError("", Some(th)).asLeft[Option[User]]
      }

    override def delete(userId: users.UserId): EitherT[F, UserIdNotFound, Unit] = ???

    override def findByLogin(login: users.Login): F[Either[AppError, Option[User]]] =
      sql.findByLogin(login).transact(transactor).attempt.map {
        case Right(op) => op.asRight[DBError]
        case Left(th) => DBError("", Some(th)).asLeft[Option[User]]
      }

    override def list: F[List[User]] = ???
  }

  def make[F[_]: MonadCancelThrow](
    sql: UserSql,
    transactor: Transactor[F]
  ) =
    new Impl(
      sql,
      transactor
    )
}
