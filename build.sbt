organization := "com.osinka.httpbl"

name := "httpbl"

homepage := Some(url("https://github.com/osinka/httpbl"))

startYear := Some(2013)

scalaVersion := "2.10.3"

crossScalaVersions := Seq("2.9.1", "2.10.3")

licenses += "Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")

organizationName := "Osinka"

description := """http:BL API"""

scalacOptions ++= List("-deprecation", "-unchecked")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.0.RC1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.1" % "test"
)

testOptions in Test += Tests.Argument("-oDS")

credentials <+= (version) map { version: String =>
  val file =
    if (version.trim endsWith "SNAPSHOT") "credentials_osinka"
    else "credentials_sonatype"
  Credentials(Path.userHome / ".ivy2" / file)
}

pomIncludeRepository := { x => false }

publishTo <<= (version) { version: String =>
  Some(
    if (version.trim endsWith "SNAPSHOT")
      "Osinka Internal Repo" at "http://repo.osinka.int/content/repositories/snapshots/"
    else
      "Sonatype OSS Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
  )
}

useGpg := true

pomExtra := <xml:group>
    <developers>
      <developer>
        <id>alaz</id>
        <email>azarov@osinka.com</email>
        <name>Alexander Azarov</name>
        <timezone>+4</timezone>
      </developer>
    </developers>
    <scm>
      <connection>scm:git:git://github.com/osinka/httpbl.git</connection>
      <developerConnection>scm:git:git@github.com:osinka/httpbl.git</developerConnection>
      <url>http://github.com/osinka/httpbl</url>
    </scm>
    <issueManagement>
      <system>github</system>
      <url>http://github.com/osinka/httpbl/issues</url>
    </issueManagement>
  </xml:group>
