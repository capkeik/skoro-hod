package domain

abstract class AppError(
  message: String = "App Error",
  cause: Option[Throwable] = None
) extends Product with Serializable
