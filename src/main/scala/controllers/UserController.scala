package controllers

import cats.Monad
import cats.data.EitherT
import cats.implicits._
import domain.auth.JwtService
import domain.users.{CreateUser, UserName}
import domain.users.errors.LoginInUse
import models.forms.SignUpForm
import service.UserService
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.{PublicEndpoint, endpoint}
import sttp.tapir.server.ServerEndpoint

trait UserController[F[_]] {
  val signUp: ServerEndpoint[Any, F]
  val all: List[ServerEndpoint[Any, F]]
}

object UserController {

  def make[F[_]: Monad](
    userService: UserService[F],
    jwtService: JwtService[F]
  ): UserController[F] =
    new Impl(userService, jwtService)
  class Impl[F[_]: Monad](userService: UserService[F], jwtService: JwtService[F]) extends UserController[F] {
    override val signUp: ServerEndpoint[Any, F] =
      endpoints.signUp.serverLogic {
        form => {
          val createUser = CreateUser(
            UserName(""),
            form.login,
            form.password,
            form.role
          )
          userService.createUser(createUser).value.map {
            case Right(_) => ().asRight[LoginInUse]
            case Left(_) => LoginInUse(form.login.value).asLeft[Unit]
          }
        }
      }

//    val idx: ServerEndpoint[Any, F] =
//      endpoints.idx.serverLogic {
//        _ => EitherT.fromEither("Hello".asRight[LoginInUse])
//      }
    override val all: List[ServerEndpoint[Any, F]] = List(
      signUp
    )
  }

  private object endpoints {
    val signUp: PublicEndpoint[SignUpForm, LoginInUse, Unit, Any] = endpoint.post
      .in("users")
      .description("Sign Up endpoint")
      .in(jsonBody[SignUpForm])
      .errorOut(jsonBody[LoginInUse])

    val idx: PublicEndpoint[Unit, LoginInUse, String, Any] = endpoint.get
      .in("home")
      .description("Sign Up endpoint")
      .errorOut(jsonBody[LoginInUse])
      .out(jsonBody[String])
  }
}
