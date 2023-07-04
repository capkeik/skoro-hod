package domain.users

import cats.Applicative
import tsec.authorization.AuthorizationInfo

import java.util.UUID

case class User(
  userName: String,
  firstName: String,
  lastName: String,
  login: String,
  id: UUID,
  role: Role)

object User {
  implicit def authRole[F[_]](implicit F: Applicative[F]): AuthorizationInfo[F, Role, User] =
    new AuthorizationInfo[F, Role, User] {
      def fetchInfo(u: User): F[Role] = F.pure(u.role)
    }
}
