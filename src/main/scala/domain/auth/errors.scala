package domain.auth

import domain.AppError

object errors {
  case class InvalidToken(message: String, cause: Option[Throwable] = None) extends AppError(message, cause)
}
