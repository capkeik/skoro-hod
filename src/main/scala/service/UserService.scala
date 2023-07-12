package service

import cats.data.{EitherT, OptionT}
import cats.implicits.{catsSyntaxEitherId, toFunctorOps}
import cats.{Applicative, Functor, Monad}
import domain.AppError
import domain.users.errors.{LoginInUse, UserError, UserIdNotFound}
import domain.users.{CreateUser, Login, User, UserId, UserRepositoryAlgebra}


class UserService[F[_]](userRepo: UserRepositoryAlgebra[F]) {
  def createUser(user: CreateUser)(implicit M: Monad[F]): EitherT[F, AppError, UserId] =
    for {
      saved <- userRepo.create(user)
    } yield saved.id

  def getUser(userId: UserId)(implicit F: Functor[F]): EitherT[F, AppError, User] = ???
//    userRepo.get(userId).map {
//      case Left(e) => EitherT.pure(e)
//      case Right(op) => op match {
//        case None => UserIdNotFound(userId.value).asLeft[User]
//        case Some(u) => u.asRight[AppError]
//      }
//    }

  def getUserByLogin(login: Login): F[Either[AppError, Option[User]]] =
    userRepo.findByLogin(login)

  def deleteUser(userId: UserId)(implicit F: Functor[F]): F[Unit] =
    userRepo.delete(userId).value.void

  def update(user: User)(implicit M: Monad[F]): EitherT[F, UserError, User] =
    for {
      saved <- userRepo.update(user)
    } yield saved

  def list: F[List[User]] =
    userRepo.list
}

object UserService {
  def apply[F[_]](
    repository: UserRepositoryAlgebra[F],
  ): UserService[F] =
    new UserService[F](repository)
}
