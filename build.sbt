import java.util

import sbt.Def
import sbtassembly.AssemblyKeys.assembly
import sbtassembly.MergeStrategy

conflictManager := ConflictManager.strict

lazy val env: util.Map[String, String] = System.getenv()
lazy val artifactoryUser: String = env.get("REPO_USER")
lazy val artifactoryToken: String = env.get("REPO_TOKEN")
lazy val akkaHttpV = "10.0.11"

lazy val commonSettings = Seq(homepage := Some(
  new URL("https://github.com/rojanu/sbt-release-test")),
  organization := "com.github.rojanu",
  description := "sbt-release artifactory test",
  scalaVersion := "2.11.8",
  resolvers += Resolver.mavenLocal,
  javacOptions ++= Seq("-encoding", "UTF-8"),
  scalacOptions := Seq("-encoding", "utf8", "-feature", "-unchecked", "-deprecation", "-target:jvm-1.8", "-language:_", "-Xlog-reflective-calls", "-Ywarn-adapted-args")
)

lazy val assemblySettings: Seq[Def.SettingsDefinition] = Seq(
  test in assembly := {},
//  assemblyJarName in assembly := s"sbt-release-test-${version.value}.jar",
//  skip in publish := true,
  assemblyMergeStrategy in assembly := {
    case "logback.xml" => MergeStrategy.first
    case x => val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  },
  artifact in(Compile, assembly) := {
    val art = (artifact in(Compile, assembly)).value
    art.withClassifier(Some("assembly"))
  },
  addArtifact(artifact in(Compile, assembly), assembly)
)

lazy val publishSettings = Seq(
  credentials += Credentials("Artifactory Realm", "malinux.ddns.net", artifactoryUser, artifactoryToken),
  publishTo := {
    val repoUrl = "http://malinux.ddns.net:8081/artifactory/"
    if (isSnapshot.value)
      Some("Artifactory snapshots" at repoUrl + "libs-snapshot-local;build.timestamp=" + new java.util.Date().getTime)
    else
      Some("Artifactory releases" at repoUrl + "libs-release")
  },
  publishMavenStyle := true,
  releaseVersionBump := sbtrelease.Version.Bump.Bugfix
)

lazy val sbtReleaseTest = project.in(file("."))
  .enablePlugins(AssemblyPlugin)
  .settings(assemblySettings: _*)
  .settings(
    commonSettings,
    publishSettings,
    name := "sbt-release-test",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % "test",
      "org.scalatest" %% "scalatest" % "3.0.4" % "test"
    )
  )


//lazy val distribution = project
//  .settings(
//    commonSettings,
//    name := "sbt-release-test",
//    publishSettings,
//    // I am sober. no dependencies.
//    packageBin in Compile := (assembly in(sbtReleaseTest, Compile)).value
//  )
