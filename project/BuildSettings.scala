import java.util

import sbt.Keys._
import sbt._
import sbtassembly.AssemblyKeys._
import sbtassembly.MergeStrategy

object BuildSettings {
  val env: util.Map[String, String] = System.getenv()
  val artifactoryUser: String = env.get("REPO_USER")
  val artifactoryToken: String = env.get("REPO_TOKEN")

  lazy val basicSettings = Seq(
    homepage := Some(new URL("https://github.com/rojanu/sbt-release-test")),
    organization := "com.github.rojanu",
    description := "sbt-release artifactory test",
    scalaVersion := "2.11.8",
    resolvers += Resolver.mavenLocal,
    javacOptions ++= Seq("-encoding", "UTF-8"),
    scalacOptions := Seq("-encoding", "utf8", "-feature", "-unchecked", "-deprecation", "-target:jvm-1.8", "-language:_", "-Xlog-reflective-calls", "-Ywarn-adapted-args"),
    credentials += Credentials("Artifactory Realm", "artv4.imedidata.net", artifactoryUser, artifactoryToken),
    publishTo := {
      val repoUrl = "http://malinux.ddns.net:8081/artifactory/"
      if (isSnapshot.value)
        Some("Artifactory snapshots" at repoUrl + "libs-snapshot-local;build.timestamp=" + new java.util.Date().getTime)
      else
        Some("Artifactory releases" at repoUrl + "libs-release-local")
    },
    assemblyJarName in assembly := s"sbt-release-test-${version.value}.jar",
    assemblyMergeStrategy in assembly := {
      case "logback.xml" => MergeStrategy.first
      case x => val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    },
    publishMavenStyle := true
  )
}
