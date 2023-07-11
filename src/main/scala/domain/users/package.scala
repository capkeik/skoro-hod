package domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.Read
import io.estatico.newtype.macros.newtype
import sttp.tapir.CodecFormat.TextPlain
import sttp.tapir.{Codec, Schema}
import tofu.logging.derivation.loggable


package object users {
  @newtype
  @derive(loggable, encoder, decoder)
  case class UserId(value: Long)

  @derive(loggable, encoder, decoder)
  @newtype
  case class Balance(value: Long)

  @derive(loggable, encoder, decoder)
  @newtype
  case class UserName(value: String)

  @derive(loggable, encoder, decoder)
  @newtype
  case class Login(value: String)

  @derive(encoder, decoder)
  @newtype
  case class Password(value: String)

  @derive(loggable, encoder, decoder)
  @newtype
  case class Role(value: String)

  object UserId {
    implicit val schema: Schema[UserId]                   = Schema
      .schemaForLong.map(l => Some(UserId(l)))(_.value)
    implicit val codec : Codec[String, UserId, TextPlain] =
      Codec.long.map(UserId(_))(_.value)

    implicit val doobieRead: Read[UserId] = Read[Long].map(UserId(_))
  }

  object Balance {
    implicit val schema: Schema[Balance]                   = Schema
      .schemaForLong.map(l => Some(Balance(l)))(_.value)
    implicit val codec : Codec[String, Balance, TextPlain] =
      Codec.long.map(Balance(_))(_.value)

    implicit val doobieRead: Read[Balance] = Read[Long].map(Balance(_))
  }

  object UserName {
    implicit val schema: Schema[UserName]                   = Schema
      .schemaForString.map(u => Some(UserName(u)))(_.value)
    implicit val codec : Codec[String, UserName, TextPlain] =
      Codec.string.map(UserName(_))(_.value)

    implicit val doobieRead: Read[UserName] = Read[String].map(UserName(_))
  }

  object Login {
    implicit val schema: Schema[Login]                   = Schema
      .schemaForString.map(u => Some(Login(u)))(_.value)
    implicit val codec : Codec[String, Login, TextPlain] =
      Codec.string.map(Login(_))(_.value)

    implicit val doobieRead: Read[Login] = Read[String].map(Login(_))
  }

  object Password {
    implicit val schema: Schema[Password]                   = Schema
      .schemaForString.map(u => Some(Password(u)))(_.value)
    implicit val codec : Codec[String, Password, TextPlain] =
      Codec.string.map(Password(_))(_.value)

    implicit val doobieRead: Read[Password] = Read[String].map(Password(_))
  }

  object Role {
    implicit val schema: Schema[Role] = Schema
      .schemaForString.map(u => Some(Role(u)))(_.value)
    implicit val codec : Codec[String, Role, TextPlain] =
      Codec.string.map(Role(_))(_.value)
    implicit val doobieRead: Read[Role] = Read[String].map(Role(_))
  }
}
