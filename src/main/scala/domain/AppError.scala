package domain

abstract class AppError(
  message: String = "App Error",
  cause: Option[Throwable] = None
) extends Product with Serializable

case class DBError(
  message: String = "",
  cause: Option[Throwable] = None
) extends AppError(message, cause)
