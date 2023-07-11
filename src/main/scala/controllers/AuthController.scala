package controllers

import cats.Monad
import cats.effect.IO
import domain.AppError
import domain.auth.errors.IncorrectCredentials
import domain.users.errors.LoginInUse
import models.forms.{SignInForm, SignUpForm}
import service.AuthService
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.{PublicEndpoint, endpoint}


trait AuthController[F[_]] {
  val signIn: ServerEndpoint[Any, F]
//  val logout: ServerEndpoint[Any, F]
  val all: List[ServerEndpoint[Any, F]]
}

object AuthController {
  class Impl(authService: AuthService[IO]) extends AuthController[IO] {
    override val signIn: ServerEndpoint[Any, IO] = endpoints.signIn
      .serverLogic(form =>
        authService.signIn(form).value
      )
//    override val logout: ServerEndpoint[Any, IO] = ???

    val all: List[ServerEndpoint[Any, IO]] = List(signIn)
  }

  def make(authService: AuthService[IO]) = new Impl(authService)

}

private object endpoints {
  val signIn: PublicEndpoint[SignInForm, IncorrectCredentials, String, Any] =
    endpoint.post
      .in("login")
      .description("Sign In endpoint")
      .in(jsonBody[SignInForm])
      .errorOut(jsonBody[IncorrectCredentials])
      .out(jsonBody[String])
}