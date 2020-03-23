addSbtPlugin("com.github.alonsodomin" % "sbt-spark" % "0.6.0")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.7")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1")


addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.23")

libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "compilerplugin" % "0.9.4"
  ,"com.thesamet.scalapb" %% "sparksql-scalapb" % "0.9.0"

)