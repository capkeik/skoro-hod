package service

import cats.Monad
import cats.data.EitherT
import cats.implicits.{catsSyntaxEitherId, toFunctorOps}
import domain.auth.JwtService
import domain.auth.errors.IncorrectCredentials
import models.forms.SignInForm

class AuthService[F[_]: Monad](userService: UserService[F], jwtService: JwtService[F]) {
  def signIn(signInForm: SignInForm): EitherT[F, IncorrectCredentials, String] = ???
//  {
//     EitherT( userService.getUserByLogin(signInForm.login).value.map {
//        case Some(u) =>
//          if (signInForm.password == u.password) jwtService.generateJwt(u.id).asRight[IncorrectCredentials]
//          else IncorrectCredentials().asLeft
//        case _ => IncorrectCredentials().asLeft
//      })
//  }
}
