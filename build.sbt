// --- project info ---
name := "macro-lms"

name := "lms-core"

organization := "org.scala-lang.lms"

/*<<<<<<< HEAD
description := "Lightweight Modular Staging"

homepage := Some(url("https://scala-lms.github.io"))

licenses := List("BSD-like" -> url("https://github.com/TiarkRompf/virtualization-lms-core/blob/develop/LICENSE"))

scmInfo := Some(ScmInfo(url("https://github.com/TiarkRompf/virtualization-lms-core"), "git@github.com:TiarkRompf/virtualization-lms-core.git"))

// developers := List(Developer("tiarkrompf", "Tiark Rompf", "@tiarkrompf", url("http://github.com/tiarkrompf")))


// --- scala settings ---

scalaVersion := virtScala
=======*/
scalaVersion := "2.11.2"

scalaOrganization := "org.scala-lang.virtualized"

scalaSource in Compile <<= baseDirectory(_ / "src")

scalaSource in Test <<= baseDirectory(_ / "test-src")

libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-library" % _ % "compile")

libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-compiler" % _ % "compile")

/*<<<<<<< HEAD
// --- dependencies ---

libraryDependencies += ("org.scala-lang.virtualized" % "scala-library" % virtScala)

// Transitive dependency through scala-continuations-library
libraryDependencies += ("org.scala-lang.virtualized" % "scala-compiler" % virtScala).
  exclude ("org.scala-lang", "scala-library").
  exclude ("org.scala-lang", "scala-compiler")

libraryDependencies += ("org.scala-lang.plugins" % "scala-continuations-library_2.11" % "1.0.2").
  exclude ("org.scala-lang", "scala-library").
  exclude ("org.scala-lang", "scala-compiler")

libraryDependencies += ("org.scalatest" % "scalatest_2.11" % "2.2.2").
  exclude ("org.scala-lang", "scala-library").
  exclude ("org.scala-lang", "scala-compiler").
  exclude ("org.scala-lang", "scala-reflect")
=======*/
libraryDependencies ++= Seq(
  "org.scala-lang.virtualized" %% "scala-virtualized" % "0.0.1-SNAPSHOT"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.0" % "test"
)

// tests are not thread safe
parallelExecution in Test := false

// disable publishing of main docs
publishArtifact in (Compile, packageDoc) := false
//>>>>>>> macro-trans

// continuations
val contVersion = "1.0.2"

autoCompilerPlugins := true

/*<<<<<<< HEAD
addCompilerPlugin("org.scala-lang.plugins" % "scala-continuations-plugin_2.11.2" % "1.0.2")

scalacOptions += "-P:continuations:enable"


// --- testing ---

// tests are not thread safe
parallelExecution in Test := false

// code coverage
scoverage.ScoverageSbtPlugin.ScoverageKeys.coverageHighlighting := false
=======*/
libraryDependencies ++= Seq(
  "org.scala-lang.plugins" %% "scala-continuations-library" % contVersion % "compile"
)

libraryDependencies <<= (scalaVersion, libraryDependencies) { (ver, deps) =>
     deps :+ compilerPlugin("org.scala-lang.plugins" % "scala-continuations-plugin" % contVersion cross CrossVersion.full)
}

scalacOptions += "-P:continuations:enable"

val paradiseVersion = "2.0.1"

libraryDependencies ++= (
  if (scalaVersion.value.startsWith("2.10")) List("org.scalamacros" %% "quasiquotes" % paradiseVersion)
  else Nil
)

libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _ % "compile")

addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full)
//>>>>>>> macro-trans
