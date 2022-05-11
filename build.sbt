name := "scala-study"
version := "0.1"
scalaVersion := "2.13.8"
val AkkaVersion = "2.6.19"
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.9.2",
   "com.eed3si9n" %% "gigahorse-okhttp" % "0.5.0",
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "org.slf4j" % "slf4j-simple" % "1.7.12",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.7" % Test
)



