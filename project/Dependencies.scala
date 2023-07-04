import sbt.*

object Dependencies {
  object V {
    lazy val catsEffect = "3.5.1"
    lazy val catsCore = "2.9.0"
    lazy val tsec = "0.4.0"
  }

  def tsec(artifact: String): ModuleID = "io.github.jmcardon" %% artifact % V.tsec

  val catsEffect = "org.typelevel" %% "cats-effect" % V.catsEffect
  val catsCore = "org.typelevel" %% "cats-core" % V.catsCore

  val allDeps: Seq[sbt.ModuleID] = Seq(
    catsEffect,
    catsCore,
    tsec("tsec-common"),
    tsec("tsec-password"),
    tsec("tsec-mac"),
    tsec("tsec-signatures"),
    tsec("tsec-jwt-mac"),
    tsec("tsec-jwt-sig"),
    tsec("tsec-http4s"),
  )
}
