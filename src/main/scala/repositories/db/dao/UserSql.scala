package repositories.db.dao

import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxEitherId}
import domain.users.errors.{LoginInUse, UserError, UserIdNotFound}
import domain.users.{Balance, CreateUser, Login, User, UserId}
import doobie.implicits.toSqlInterpolator
import doobie.util.update.{Update, Update0}
import doobie.{ConnectionIO, Query0}

trait UserSql {
  def create(createUser: CreateUser): ConnectionIO[Either[LoginInUse, User]]

  def update(user: User): ConnectionIO[Either[UserError, User]]

  def get(userId: UserId): ConnectionIO[Option[User]]

  def delete(userId: UserId): ConnectionIO[Either[UserIdNotFound, Unit]]

  def findByLogin(login: Login): ConnectionIO[Option[User]]

  def list: ConnectionIO[List[User]]
}

object UserSql {

  def make: UserSql = new Impl

  private final class Impl extends UserSql {
    import sqls._
    override def create(createUser: CreateUser): ConnectionIO[Either[LoginInUse, User]] = {
      findByLoginSql(createUser.login).option.flatMap {
        case None => createUserSql(createUser)
          .withUniqueGeneratedKeys[UserId]("user_id").map(
          id => User(createUser.userName, createUser.login, createUser.password, id, createUser.role, Balance(0))
            .asRight[LoginInUse]
        )
        case Some(u) => LoginInUse(u.login.value).asLeft[User].pure[ConnectionIO]
      }
    }

    override def update(user: User): ConnectionIO[Either[UserError, User]] = ???
//      findByIdSql(user.id).option.map {
//        case None => UserIdNotFound(user.id.value).asLeft[User]
//        case Some(_) =>
//          findByLoginSql(user.login).option.flatMap {
//            case Some(_) => LoginInUse(user.login.value).asLeft[User].pure[ConnectionIO]
//            case None => updateSql(user).run.
//          }
//      }

    override def get(userId: UserId): ConnectionIO[Option[User]] =
      findByIdSql(userId).option

    override def delete(userId: UserId): ConnectionIO[Either[UserIdNotFound, Unit]] =
      deleteSql(userId).run.map {
        case 0 => UserIdNotFound(userId.value).asLeft[Unit]
        case _ => ().asRight[UserIdNotFound]
      }

    override def list: ConnectionIO[List[User]] =
      listSql.to[List]

    override def findByLogin(login: Login): ConnectionIO[Option[User]] =
      sqls.findByLoginSql(login).option
  }

  private object sqls {

    def createUserSql(user: CreateUser): Update0 = {
      val isAdmin = user.role.value match {
        case "admin" => true
        case "customer" => false
      }
      sql"""
      insert
      into users (user_name,
                  login,
                  is_admin,
                  passwd)

      values (${user.userName.value},
              ${user.login.value},
              $isAdmin,
              ${user.password.value})
         """.update
    }
    def updateSql(user: User): Update0 =
      sql"""
           update users
            set user_name = ${user.userName.value},
                login = ${user.login.value},
                passwd = ${user.password.value},
                balance = ${user.balance.value}
            where user_id = ${user.id.value}
         """.update

    def findByIdSql(id: UserId): Query0[User] =
      sql"""
           select *
            from users
            where user_id = ${id.value}
         """.query

    def findByLoginSql(login: Login): Query0[User] =
      sql"""
           select *
            from users
            where login = ${login.value}
         """.query[User]

    def listSql: Query0[User] =
      sql"""
           select *
           from users
         """.query[User]

    def deleteSql(id: UserId): Update0 =
      sql"""
           delete from users
           where user_id = ${id.value}
         """.update
  }
}