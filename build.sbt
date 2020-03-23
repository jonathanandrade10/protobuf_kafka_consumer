name := "proto_scala_consumer"

version := "0.1"

scalaVersion := "2.11.0"

sparkVersion := "2.3.0"

enablePlugins(SparkPlugin)
sparkComponents ++= List("core", "streaming", "streaming-kafka-0-10","sql")

sparkExclusionRules += ExclusionRule(organization = "net.jpountz.lz4", name = "lz4")


libraryDependencies ++= Seq(  "com.thesamet.scalapb" %% "sparksql-scalapb" % "0.9.0"
)


PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.google.protobuf.**" -> "shadeproto.@1").inAll




)
/*
lazy val deps = (project in file("."))
  .enablePlugins(SparkPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "com.thesamet.scalapb" %% "sparksql-scalapb" % "0.9.0"
      ,"com.thesamet.scalapb" %% "compilerplugin" % "0.9.4"

    ))
*/

/*
libraryDependencies ++= Seq("org.apache.spark" %% "spark-core" % "2.3.0" % "provided"
      ,"org.apache.spark" %% "spark-streaming" % "2.3.0" % "provided"
      //,"org.apache.spark" %% "spark-streaming-kafka-0-8" % "2.4.5"
      ,"org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.3.0"
      //,"org.apache.spark" %% "spark-streaming-kafka" % "1.6.0"
      //,"org.apache.spark" %% "spark-streaming-kafka" % "1.6.1"

    )
 */
