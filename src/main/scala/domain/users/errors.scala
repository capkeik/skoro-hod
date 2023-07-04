package domain.users

import domain.AppError

import java.util.UUID

object errors {
  sealed abstract class UserError(
    val message: String,
    val cause: Option[Throwable] = None
  ) extends AppError(message, cause)

  case class UserIdNotFound(id: UUID)
    extends UserError(s"User with id $id not found")

  case class LoginInUse(login: String)
    extends UserError(s"$login already in use")
}
