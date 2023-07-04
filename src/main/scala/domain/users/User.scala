package domain.users

import cats.Applicative
import derevo.circe.{decoder, encoder}
import derevo.derive
import io.estatico.newtype.macros.newtype
import tofu.logging.derivation._
import tsec.authorization.AuthorizationInfo

import java.util.UUID

@newtype
@derive(loggable, encoder, decoder)
case class UserId(value: UUID)

@newtype
@derive(loggable, encoder, decoder)
case class UserName(value: String)

@newtype
@derive(loggable, encoder, decoder)
case class Login(value: String)

@newtype
@derive(encoder, decoder)
case class Password(value: String)

case class User(
  userName: UserName,
  login: Login,
  password: Password,
  id: UserId,
  role: Role)

object User {
  implicit def authRole[F[_]](implicit F: Applicative[F]): AuthorizationInfo[F, Role, User] =
    (u: User) => F.pure(u.role)
}
