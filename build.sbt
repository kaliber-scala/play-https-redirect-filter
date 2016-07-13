releaseSettings

ReleaseKeys.crossBuild := true

name := "play-https-redirect-filter"

scalaVersion := "2.11.7"

organization := "net.kaliber"

crossScalaVersions := Seq("2.10.6", scalaVersion.value)

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)

val playVersion = "2.5.4"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % playVersion % "provided",
  "com.typesafe.play" %% "play-test" % playVersion % "test",
  "org.qirx" %% "little-spec" % "0.4" % "test"
)

testFrameworks += new TestFramework("org.qirx.littlespec.sbt.TestFramework")

publishTo := kaliberRepo(version.value)

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

def kaliberRepo(version: String) = {
  val repo = if (version endsWith "SNAPSHOT") "snapshot" else "release"
  Some(s"Kaliber ${repo.capitalize} Repository" at s"https://jars.kaliber.io/artifactory/libs-$repo-local")
}
