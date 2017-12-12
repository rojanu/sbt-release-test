import BuildSettings._
import Dependencies._

conflictManager := ConflictManager.strict

lazy val strategicMonitoring = project.in(file("."))
  .settings(
    basicSettings,
    name := "sbt-release-test",
    libraryDependencies ++=
      Dependencies.compile(akkaHttp, akkaJson) ++
        Dependencies.test(akkaHttpTestKit, scalaTest)
  )