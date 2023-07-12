package domain.auth

import domain.AppError
import io.circe.{Decoder, Encoder}
import io.circe.derivation.{deriveDecoder, deriveEncoder}

object errors {
  abstract class AuthError(message: String, cause: Option[Throwable] = None) extends AppError(message, cause)
  case class InvalidToken(message: String, cause: Option[Throwable] = None) extends AuthError(message, cause)
  case class IncorrectCredentials(message: String = "") extends AuthError(s"Credentials are incorrect: $message")
  object IncorrectCredentials {
    implicit val enc: Encoder[IncorrectCredentials] = deriveEncoder
    implicit val dec: Decoder[IncorrectCredentials] = deriveDecoder
  }
}
