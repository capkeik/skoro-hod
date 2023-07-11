package controllers

import cats.Monad
import cats.data.EitherT
import cats.implicits.catsSyntaxEitherId
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
    override val signUp: ServerEndpoint[Any, F]                                                                  =
      endpoints.signUp.serverLogic {
        form => {
          val createUser = CreateUser(
            UserName(""),
            form.login,
            form.password,
            form.role
          )
          val fu = userService.createUser(createUser).value
          utils.transform(fu) {
            case Left(error) => LoginInUse(error.toString).asLeft[String]
            case Right(id) => jwtService.generateJwt(id).asRight[LoginInUse]
          }
        }
      }
    override val all: List[ServerEndpoint[Any, F]] = List(
      signUp
    )
  }

  private object endpoints {
    val signUp: PublicEndpoint[SignUpForm, LoginInUse, String, Any] = endpoint.post
      .in("users")
      .description("Sign Up endpoint")
      .in(jsonBody[SignUpForm])
      .errorOut(jsonBody[LoginInUse])
      .out(jsonBody[String])
  }
}
