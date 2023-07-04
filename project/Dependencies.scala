import sbt.*

object Dependencies {
  object V {
    lazy val catsEffect = "3.5.1"
    lazy val catsCore = "2.9.0"
    lazy val tsec = "0.4.0"
    lazy val tofu = "0.12.0.1"
    lazy val tofuDerevo = "0.13.0"
    lazy val newtype = "0.4.4"
    lazy val circe = "0.14.5"
  }

  def tsec(artifact: String): ModuleID = "io.github.jmcardon" %% artifact % V.tsec
  def tofu(artifact: String): ModuleID = "tf.tofu" %% artifact % V.tofu

  val catsEffect = "org.typelevel" %% "cats-effect" % V.catsEffect
  val catsCore = "org.typelevel" %% "cats-core" % V.catsCore
  val tofuDerevo = "tf.tofu" %% "derevo-circe" % V.tofuDerevo
  val newtype = "io.estatico" %% "newtype" % V.newtype

  val allDeps: Seq[sbt.ModuleID] = Seq(
    catsEffect,
    catsCore,
    newtype,
    tofuDerevo,
    tofu("tofu-kernel"),
    tofu("tofu-logging-derivation"),
    tofu("tofu-logging-layout"),
    tofu("tofu-logging"),
    tofu("tofu-logging-logstash-logback"),
    tofu("tofu-logging-structured"),
    tofu("tofu-core-ce3"),
    tofu("tofu-doobie-logging-ce3"),
    tsec("tsec-common"),
    tsec("tsec-password"),
    tsec("tsec-mac"),
    tsec("tsec-signatures"),
    tsec("tsec-jwt-mac"),
    tsec("tsec-jwt-sig"),
    tsec("tsec-http4s"),
  )
}
