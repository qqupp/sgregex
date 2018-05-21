import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT",
      organization := "com.qqupp",
      licenses += ("MIT", url("https://opensource.org/licenses/MIT")),
      scmInfo := Some(
        ScmInfo(url("https://github.com/qqupp/sgregexp"),
          "scm:git:git@github.com:qqupp/sgregexp.git"))
    )),
    name := "sgregexp",
    libraryDependencies += scalaTest % Test
  )
