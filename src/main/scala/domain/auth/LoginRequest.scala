package domain.auth

import domain.users.{Login, Password}
final case class LoginRequest(
  login: Login,
  password: Password
)
