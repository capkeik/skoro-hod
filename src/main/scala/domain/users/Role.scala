package domain.users

import cats.Eq

final case class Role(roleRepr: String)

object Role {
  val Admin: Role = Role("admin")
  val Customer: Role = Role("customer")

  implicit val eqRole: Eq[Role] = Eq.fromUniversalEquals[Role]
}