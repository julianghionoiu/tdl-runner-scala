name := "tdl-runner-scala"

version := "1.0"

scalaVersion := "2.12.3"

resolvers += "TDL" at "http://dl.bintray.com/julianghionoiu/maven"

def scalatest = "org.scalatest" %% "scalatest" % "3.0.4" % "test"
def scalareflect = "org.scala-lang" % "scala-reflect" % "2.12.3"
def scalaxml = "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6"
def client = "ro.ghionoiu" % "tdl-client-java" % "0.24.0"
def guava = "com.google.guava" % "guava" % "18.0"
def unirest = "com.mashape.unirest" % "unirest-java" % "1.4.9"

scalacOptions += "-Xexperimental"

libraryDependencies ++= Seq(
  scalareflect,
  scalaxml,
  client,
  guava,
  unirest,
  scalatest
)

coverageEnabled := true

lazy val tdlTests = taskKey[Unit]("Run tests for CI")

tdlTests := {
  // Capture the test result
  val testResult = (test in Test).result.value
}
