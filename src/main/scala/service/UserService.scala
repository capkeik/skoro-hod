package service

import cats.data.EitherT
import cats.implicits.toFunctorOps
import cats.{Functor, Monad}
import domain.users.errors.{LoginInUse, UserError, UserIdNotFound}
import domain.users.{User, UserId, UserRepositoryAlgebra}


class UserService[F[_]](userRepo: UserRepositoryAlgebra[F]) {
  def createUser(user: User)(implicit M: Monad[F]): EitherT[F, LoginInUse, User] =
    for {
      saved <- userRepo.create(user)
    } yield saved

  def getUser(userId: UserId)(implicit F: Functor[F]): EitherT[F, UserIdNotFound, User] =
    userRepo.get(userId).toRight(UserIdNotFound(userId.value))

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
