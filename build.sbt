name := "tdl-warmup-scala"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "TDL" at "http://dl.bintray.com/julianghionoiu/maven"

def scalatest = "org.scalatest" %% "scalatest" % "2.2.4" % "test"
def scalareflect = "org.scala-lang" % "scala-reflect" % "2.11.8"
def scalaxml = "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4"
def client = "ro.ghionoiu" % "tdl-client-java" % "0.12.1"
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
