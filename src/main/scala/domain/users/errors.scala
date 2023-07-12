package domain.users

import domain.AppError
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema

import java.util.UUID

object errors {
  sealed abstract class UserError(
    val message: String,
    val cause: Option[Throwable] = None
  ) extends AppError(message, cause)

  case class UserIdNotFound(id: Long)
    extends UserError(s"User with id $id not found")

  case class LoginInUse(login: String)
    extends UserError(s"$login already in use")

  object LoginInUse {
    implicit val schema: Schema[LoginInUse]  = Schema.derived
    implicit val enc   : Encoder[LoginInUse] = deriveEncoder
    implicit val dec   : Decoder[LoginInUse] = deriveDecoder
  }
}
