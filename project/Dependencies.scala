import sbt._

object Dependencies {

  def compile(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")

  def provided(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")

  def test(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")

  def runtime(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")

  def container(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")

  private object Version {
    val akkaHttpV = "10.0.11"
    val scalaTestV = "3.0.4"
  }

  val akkaHttp: ModuleID = "com.typesafe.akka" %% "akka-http" % Version.akkaHttpV
  val akkaJson: ModuleID = "com.typesafe.akka" %% "akka-http-spray-json" % Version.akkaHttpV
  val akkaHttpTestKit: ModuleID = "com.typesafe.akka" %% "akka-http-testkit" % Version.akkaHttpV
  val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % Version.scalaTestV

}
