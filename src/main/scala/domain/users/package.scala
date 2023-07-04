package domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import io.estatico.newtype.macros.newtype
import tofu.logging.derivation.loggable

import java.util.UUID

package object users {
  @newtype
  @derive(loggable, encoder, decoder)
  case class UserId(value: UUID)

  @derive(loggable, encoder, decoder)
  @newtype
  case class UserName(value: String)

  @derive(loggable, encoder, decoder)
  @newtype
  case class Login(value: String)

  @derive(encoder, decoder)
  @newtype
  case class Password(value: String)
}
