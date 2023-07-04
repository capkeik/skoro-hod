package domain.auth

import domain.users.{Role, User, UserName, Login, Password, UserId}
import tsec.passwordhashers.PasswordHash


final case class SignupRequest(
  userName: UserName,
  login: Login,
  password: Password,
  role: Role,
) {
  def asUser[A](hashedPassword: PasswordHash[A], id: UserId): User = User(
    userName,
    login,
    Password(hashedPassword),
    id,
    role
  )
}
