import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT",
      organization := "com.github.qqupp",
      licenses += ("MIT", url("https://opensource.org/licenses/MIT")),
      scmInfo := Some(
        ScmInfo(url("https://github.com/qqupp/sgregexp"),
          "scm:git:git@github.com:qqupp/sgregexp.git"))
    )),
    name := "sgregexp",
    libraryDependencies += scalaTest % Test,
    publishSettings
  )

lazy val publishSettings = Seq(
  licenses := Seq("MIT" ->  url("https://opensource.org/licenses/MIT")),
  homepage := Some(url("https://github.com/qqupp/sgregexp")),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if(isSnapshot.value)
      Some("Snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("Releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
    <developers>
      <developer>
        <id>qqupp</id>
        <url>https://github.com/qqupp/sgregexp</url>
      </developer>
    </developers>
    ),
)

credentials in ThisBuild += Credentials(Path.userHome / ".sbt" / ".credentials")