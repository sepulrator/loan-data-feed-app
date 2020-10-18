name := "loan-data-feed-app"

version := "0.2"

scalaVersion := "2.12.12"


libraryDependencies ++= Seq(
  "software.amazon.awssdk" % "firehose" % "2.15.9",
  "com.typesafe" % "config" % "1.4.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

)