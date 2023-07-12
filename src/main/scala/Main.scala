import cats.effect.{ExitCode, IO, IOApp}
import controllers.{AuthController, UserController}
import domain.auth.JwtService
import doobie.util.transactor.Transactor
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import org.http4s.implicits._
import repositories.db.UserRepositoryDB
import repositories.db.dao.UserSql
import domain.users.{CreateUser, Login, Password, Role, UserName}
import service.{AuthService, UserService}
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter


object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    (for {
      _ <- IO.pure()
      transactor = Transactor
        .fromDriverManager[IO](
          "org.postgresql.Driver",
          "jdbc:postgresql://localhost:5432/skoro_hod",
          "capkeik",
          "cap"
        )
      sql = UserSql.make
      userRep = UserRepositoryDB.make(
        sql,
        transactor
      )
      userService = new UserService[IO](userRep)
      jwtService = new JwtService[IO]
      authService = new AuthService[IO](userService, jwtService)
      authController = AuthController.make(authService)
      userController = UserController.make(
        userService,
        jwtService
      )
      routeL: List[ServerEndpoint[Any, IO]] = userController.all ++ authController.all
      authRoutes = Http4sServerInterpreter[IO]().toRoutes(authController.all)
      routes = Http4sServerInterpreter[IO]().toRoutes(userController.all)
      httpApp = Router[IO]("/" -> routes,
      "/auth" -> authRoutes).orNotFound

      _ <- EmberServerBuilder
        .default[IO]
        .withHttpApp(httpApp)
        .build.useForever
    } yield ()).as(ExitCode.Success)
}
