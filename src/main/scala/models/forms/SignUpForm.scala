package models.forms

import derevo.circe.{decoder, encoder}
import derevo.derive
import domain.users.{Login, Password, Role}
import io.circe.{Decoder, Encoder}
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import sttp.tapir.Schema

case class SignUpForm(
  login: Login,
  password: Password,
  role: Role
)

object SignUpForm {
  implicit val schema: Schema[SignUpForm]  = Schema.derived
  implicit val enc   : Encoder[SignUpForm] = deriveEncoder
  implicit val dec   : Decoder[SignUpForm] = deriveDecoder
}
