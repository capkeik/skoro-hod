package models.forms

import domain.users.{Login, Password}
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema

case class SignInForm(
  login: Login,
  password: Password
)

object SignInForm {
  implicit val schema: Schema[SignInForm] = Schema.derived
  implicit val enc: Encoder[SignInForm] = deriveEncoder
  implicit val dec: Decoder[SignInForm] = deriveDecoder
}
