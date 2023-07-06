package domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import io.estatico.newtype.macros.newtype
import sttp.tapir.CodecFormat.TextPlain
import sttp.tapir.{Codec, Schema}
import tofu.logging.derivation.loggable

import java.util.UUID

package object users {
  @newtype
  @derive(loggable, encoder, decoder)
  case class UserId(value: UUID)

  object UserId {
    implicit val schema: Schema[UserId] = Schema
      .schemaForUUID.map(u => Some(UserId(u)))(_.value)
    implicit val codec: Codec[String, UserId, TextPlain] =
      Codec.uuid.map(UserId(_))(_.value)
  }

  @derive(loggable, encoder, decoder)
  @newtype
  case class UserName(value: String)

  object UserName {
    implicit val schema: Schema[UserName] = Schema
      .schemaForString.map(u => Some(UserName(u)))(_.value)
    implicit val codec: Codec[String, UserName, TextPlain] =
      Codec.string.map(UserName(_))(_.value)
  }

  @derive(loggable, encoder, decoder)
  @newtype
  case class Login(value: String)

  object Login {
    implicit val schema: Schema[Login] = Schema
      .schemaForString.map(u => Some(Login(u)))(_.value)
    implicit val codec: Codec[String, Login, TextPlain] =
      Codec.string.map(Login(_))(_.value)
  }

  @derive(encoder, decoder)
  @newtype
  case class Password(value: String)

  object Password {
    implicit val schema: Schema[Password] = Schema
      .schemaForString.map(u => Some(Password(u)))(_.value)
    implicit val codec: Codec[String, Password, TextPlain] =
      Codec.string.map(Password(_))(_.value)
  }
}
