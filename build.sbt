name := "loan-data-feed-app"

version := "0.1"

scalaVersion := "2.12.8"


lazy val alpakkaV = "0.20"

resolvers += Resolver.bintrayRepo("akka", "snapshots")

libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-kinesis" % alpakkaV,
  "com.lightbend.akka" %% "akka-stream-alpakka-file" % alpakkaV,
  "com.lightbend.akka" %% "akka-stream-alpakka-csv" % alpakkaV
)