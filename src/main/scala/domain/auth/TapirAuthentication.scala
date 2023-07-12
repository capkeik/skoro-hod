package domain.auth

import cats.Monad
import cats.data.EitherT
import domain.auth.errors.InvalidToken
import domain.users.UserId

class TapirAuthentication[F[_]: Monad](jwtService: JwtService[F]) {
  def authenticate(token: String): EitherT[F, InvalidToken, UserId] = {
    jwtService.extractUserIdFromJwt(token)
  }
}
