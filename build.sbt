
lazy val commonSettings = Seq(
  organization := "net.nomadicalien",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.0",
  addCompilerPlugin("org.psywerx.hairyfotr" %% "linter" % "0.1.17")
)

lazy val librarySettings = Seq(
  "org.specs2"     %% "specs2-core"       % "3.8.6"  % Test,
  "org.specs2"     %% "specs2-scalacheck" % "3.8.6"  % Test,
  "org.scalacheck" %% "scalacheck"        % "1.13.4" % Test
)

lazy val root = (project in file(".")).settings(commonSettings: _*).aggregate(chapter1)

lazy val chapter1 = (project in file("chapter1"))
  .settings(commonSettings: _*)
  .settings(name := "chapter1")
  .settings(libraryDependencies ++= librarySettings)
  .settings(scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xlint"))
  .settings(scalacOptions in Test ++= Seq("-Yrangepos", "-Xlint"))