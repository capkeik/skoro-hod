package domain.users

import cats.Eq

final case class Role(roleRepr: String)

object Role {
  val Admin: Role = Role("Admin")
  val Customer: Role = Role("Customer")

  implicit val eqRole: Eq[Role] = Eq.fromUniversalEquals[Role]
}