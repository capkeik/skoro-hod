package domain.users

case class User(
  userName: UserName,
  login: Login,
  password: Password,
  id: UserId,
  role: Role
)

case class CreateUser(
  userName: UserName,
  login: Login,
  password: Password,
  role: Role
)

