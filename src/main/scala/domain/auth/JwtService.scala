package domain.auth

import cats.Applicative
import cats.data.EitherT
import cats.implicits.catsSyntaxEitherId
import domain.AppError
import domain.auth.errors.InvalidToken
import domain.users.UserId
import io.jsonwebtoken.{Claims, Jwts, SignatureAlgorithm}
import service.UserService

import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.{Date, UUID}
import scala.util.{Failure, Success, Try}

class JwtService[F[_] : Applicative](userService: UserService[F]) {

  /** Jwt token secret */
  private val secret = "secret secretsecret secret"
  /** Jwt token live duration */
  private val ttlSeconds = 3600000

  /** Generates jwt with user id in it. */
  def generateJwt(userId: UserId): String = {
    val now = Instant.now
    val jwt = Jwts.builder()
      .setId(UUID.randomUUID.toString) // id for jwt
      .setIssuedAt(Date.from(now)) // time from which token is active
      .setExpiration(Date.from(now.plusSeconds(ttlSeconds))) // time to which token is active
      .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8.toString)
      ).claim("userId", userId.value) // adding claim
    jwt.compact()
  }

  /**
   * Extracts user from jwt token
   *
   * @param jwt token from authorization header.
   * @param ec  for async futures.
   * @return either exception or optional user.
   */
  def extractUserIdFromJwt(jwt: String): EitherT[F, AppError, UserId] = {
    val decodedJwtStr =
      URLDecoder.decode(jwt, StandardCharsets.UTF_8.toString)
    Try {
      Jwts
        .parser()
        .setSigningKey(secret.getBytes(StandardCharsets.UTF_8.toString))
        .parseClaimsJws(decodedJwtStr)
    } match {
      case Failure(exception) => EitherT.fromEither(InvalidToken(s"Exception while decoding JWT $jwt", Some(exception)).asLeft[UserId])
      case Success(claims) =>
        val jwtClaims: Claims = claims.getBody
        jwtClaims.get("userId") match {
          case Some(stringId) => {
            val id = UserId(UUID.fromString(stringId.toString))
            EitherT.fromEither(Right(id))
          }
          case None => EitherT.fromEither(InvalidToken(s"Could not get user from claim $jwtClaims").asLeft[UserId])
        }
    }
  }
}
