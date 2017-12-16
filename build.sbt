import java.util

import sbtassembly.AssemblyKeys.assembly
import sbtassembly.MergeStrategy

conflictManager := ConflictManager.strict

val env: util.Map[String, String] = System.getenv()
val artifactoryUser: String = env.get("REPO_USER")
val artifactoryToken: String = env.get("REPO_TOKEN")
val akkaHttpV = "10.0.11"

lazy val sbtReleaseTest = project.in(file("."))
  .enablePlugins(AssemblyPlugin)
  .settings(
    homepage := Some(new URL("https://github.com/rojanu/sbt-release-test")),
    organization := "com.github.rojanu",
    description := "sbt-release artifactory test",
    scalaVersion := "2.11.8",
    resolvers += Resolver.mavenLocal,
    javacOptions ++= Seq("-encoding", "UTF-8"),
    scalacOptions := Seq("-encoding", "utf8", "-feature", "-unchecked", "-deprecation", "-target:jvm-1.8", "-language:_", "-Xlog-reflective-calls", "-Ywarn-adapted-args"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % "test",
      "org.scalatest" %% "scalatest" % "3.0.4" % "test"
    ),
    assemblyJarName in assembly := s"sbt-release-test-${version.value}.jar",
    assemblyMergeStrategy in assembly := {
      case "logback.xml" => MergeStrategy.first
      case x => val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    },
    skip in publish := true
  )


lazy val cosmetic = project
  .settings(
    homepage := Some(new URL("https://github.com/rojanu/sbt-release-test")),
    organization := "com.github.rojanu",
    description := "sbt-release artifactory test",
    scalaVersion := "2.11.8",
    resolvers += Resolver.mavenLocal,
    javacOptions ++= Seq("-encoding", "UTF-8"),
    scalacOptions := Seq("-encoding", "utf8", "-feature", "-unchecked", "-deprecation", "-target:jvm-1.8", "-language:_", "-Xlog-reflective-calls", "-Ywarn-adapted-args"),
    name := "sbt-release-test",
    credentials += Credentials("Artifactory Realm", "malinux.ddns.net", "admin", "APqRdKPASWN9W4AnTmCyBYkhi6"),
    publishTo := {
      val repoUrl = "http://malinux.ddns.net:8081/artifactory/"
      if (isSnapshot.value)
        Some("Artifactory snapshots" at repoUrl + "libs-snapshot-local;build.timestamp=" + new java.util.Date().getTime)
      else
        Some("Artifactory releases" at repoUrl + "libs-release")
    },
    publishMavenStyle := true,
    // I am sober. no dependencies.
    packageBin in Compile := (assembly in(sbtReleaseTest, Compile)).value
  )
