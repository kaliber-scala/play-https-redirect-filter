releaseSettings

ReleaseKeys.crossBuild := true

name := "play-https-redirect-filter"

scalaVersion := "2.11.5"

organization := "nl.rhinofly"

crossScalaVersions := Seq("2.10.4", scalaVersion.value)

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  rhinoflyRepo("RELEASE").get
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.4.2" % "provided"
)

publishTo := rhinoflyRepo(version.value)

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

def rhinoflyRepo(version: String) = {
  val repo = if (version endsWith "SNAPSHOT") "snapshot" else "release"
  Some("Rhinofly Internal " + repo.capitalize + " Repository" at "http://maven-repository.rhinofly.net:8081/artifactory/libs-" + repo + "-local")
}