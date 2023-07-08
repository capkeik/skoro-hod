import cats.effect.{ExitCode, IO, IOApp}
import controllers.AuthController
import domain.auth.JwtService
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import org.http4s.implicits._
import repositories.inmemory.UserInMemoryRepository
import service.{AuthService, UserService}
import sttp.tapir.server.http4s.Http4sServerInterpreter


object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    (for {
      _ <- IO.pure()
      userRep = UserInMemoryRepository.make
      userService = new UserService[IO](userRep)
      jwtService = new JwtService[IO]
      authService = new AuthService[IO](userService, jwtService)
      controller = AuthController.make(authService)
      routes = Http4sServerInterpreter[IO].toRoutes(controller.signIn)
      httpApp = Router[IO]("/" -> routes).orNotFound

      _ <- EmberServerBuilder
        .default[IO]
        .withHttpApp(httpApp)
        .build.useForever
    } yield ()).as(ExitCode.Success)
}
