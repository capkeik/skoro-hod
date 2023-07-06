import sbt.*

object Dependencies {
  object V {
    lazy val catsEffect = "3.5.1"
    lazy val catsCore = "2.9.0"
    lazy val tofu = "0.12.0.1"
    lazy val tofuDerevo = "0.13.0"
    lazy val newtype = "0.4.4"
    lazy val circe = "0.14.5"
    lazy val jwt = "0.9.1"
    lazy val tapir = "1.4.0"
  }

  def tofu(artifact: String): ModuleID = "tf.tofu" %% artifact % V.tofu
  def tapir(artifact: String): ModuleID = "com.softwaremill.sttp.tapir" %% s"tapir-$artifact" % V.tapir

  val catsEffect = "org.typelevel" %% "cats-effect" % V.catsEffect
  val catsCore = "org.typelevel" %% "cats-core" % V.catsCore
  val tofuDerevo = "tf.tofu" %% "derevo-circe" % V.tofuDerevo
  val newtype = "io.estatico" %% "newtype" % V.newtype
  val jsonWebToken = "io.jsonwebtoken" % "jjwt" % V.jwt

  val allDeps: Seq[sbt.ModuleID] = Seq(
    catsEffect,
    catsCore,
    newtype,
    tofuDerevo,
    jsonWebToken,
    tofu("tofu-kernel"),
    tofu("tofu-logging-derivation"),
    tofu("tofu-logging-layout"),
    tofu("tofu-logging"),
    tofu("tofu-logging-logstash-logback"),
    tofu("tofu-logging-structured"),
    tofu("tofu-core-ce3"),
    tofu("tofu-doobie-logging-ce3"),
    tapir("core"),
    tapir("http4s-server"),
    tapir("json-circe")
  )
}
