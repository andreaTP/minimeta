
lazy val root = project.in(file(".")
  ) aggregate (
    commonJVM,
    commonJS,
    dialectsJVM,
    dialectsJS,
    inputsJVM,
    inputsJS,
    inlineJVM,
    inlineJS,
    tokensJVM,
    tokensJS,
    tokenizersJVM,
    tokenizersJS,
    treesJVM,
    treesJS,
    transversersJVM,
    transversersJS,
    parsersJVM,
    parsersJS,
    quasiquotesJVM,
    quasiquotesJS,
    scalametaJVM,
    scalametaJS
  )

lazy val common = crossProject.in(file("scalameta/common")
  ) settings (
    name := "common"
  ) jvmSettings (
    jvmSharedSettings : _*
  ) jsSettings(
    jsSharedSettings : _*
  ) settings (
    description := "Bag of private and public helpers used in scala.meta's APIs and implementations",
    enableMacros
  )

lazy val commonJS = common.js
lazy val commonJVM = common.jvm

lazy val dialects = crossProject.in(file("scalameta/dialects")
  ) settings (
    name := "dialects"
  ) jvmSettings (
    jvmSharedSettings : _*
  ) jsSettings(
    jsSharedSettings : _*
  ) settings (
    description := "Scala.meta's dialects",
    enableMacros
  ) dependsOn (common)

lazy val dialectsJS = dialects.js
lazy val dialectsJVM = dialects.jvm

lazy val inputs = crossProject.in(file("scalameta/inputs")
  ) settings (
    name := "inputs"
  ) jvmSettings (
    jvmSharedSettings : _*
  ) jsSettings(
    jsSharedSettings : _*
  ) settings (
    description := "Scala.meta's APIs for source code in textual format"
  ) dependsOn (common)

lazy val inputsJS = inputs.js
lazy val inputsJVM = inputs.jvm

lazy val inline = crossProject.in(file("scalameta/inline")
  ) settings (
    name := "inline"
  ) jvmSettings (
    jvmSharedSettings : _*
  ) jsSettings(
    jsSharedSettings : _*
  ) settings (
    description := "Scala.meta's APIs for new-style (\"inline\") macros"
  ) dependsOn (inputs)

lazy val inlineJS = inline.js
lazy val inlineJVM = inline.jvm

lazy val tokens = crossProject.in(file("scalameta/tokens")
  ) settings (
    name := "tokens"
  ) jvmSettings (
    jvmSharedSettings : _*
  ) jsSettings(
    jsSharedSettings : _*
  ) settings (
    description := "Scala.meta's tokens and token-based abstractions (inputs and positions)",
    enableMacros
  ) dependsOn (common, dialects, inputs)

lazy val tokensJS = tokens.js
lazy val tokensJVM = tokens.jvm

lazy val tokenizers = crossProject.in(file("scalameta/tokenizers")
  ) settings (
    name := "tokenizers"
  ) jvmSettings (
    jvmSharedSettings : _*
  ) jsSettings(
    jsSharedSettings : _*
  ) settings (
    description := "Scala.meta's APIs for tokenization and its baseline implementation",
    enableMacros
  ) jvmSettings (
    libraryDependencies += "com.lihaoyi" %% "scalaparse" % "0.3.7"
  ) jsSettings (
    libraryDependencies += "com.lihaoyi" %%% "scalaparse" % "0.3.7"
  ) dependsOn (tokens)

lazy val tokenizersJS = tokenizers.js
lazy val tokenizersJVM = tokenizers.jvm

lazy val trees = crossProject.in(file("scalameta/trees")
  ) settings (
    name := "trees"
  ) jvmSettings (
    jvmSharedSettings : _*
  ) jsSettings(
    jsSharedSettings : _*
  ) settings (
    description := "Scala.meta's abstract syntax trees",
    // NOTE: uncomment this to update ast.md
    // scalacOptions += "-Xprint:typer",
    enableMacros
  ) dependsOn (tokenizers) // NOTE: tokenizers needed for Tree.tokens when Tree.pos.isEmpty

lazy val treesJS = trees.js
lazy val treesJVM = trees.jvm

lazy val transversers = crossProject.in(file("scalameta/transversers")
  ) settings (
    name := "transversers"
  ) jvmSettings (
    jvmSharedSettings : _*
  ) jsSettings(
    jsSharedSettings : _*
  ) settings (
    description := "Scala.meta's traversal and transformation infrastructure for abstract syntax trees",
    enableMacros
  ) dependsOn (trees)

lazy val transversersJS = transversers.js
lazy val transversersJVM = transversers.jvm

lazy val parsers = crossProject.in(file("scalameta/parsers")
  ) settings (
    name := "parsers"
  ) jvmSettings (
    jvmSharedSettings : _*
  ) jsSettings(
    jsSharedSettings : _*
  ) settings (
    description := "Scala.meta's API for parsing and its baseline implementation"
  ) dependsOn (trees)

lazy val parsersJS = parsers.js
lazy val parsersJVM = parsers.jvm

lazy val quasiquotes = crossProject.in(file("scalameta/quasiquotes")
  ) settings (
    name := "quasiquotes"
  ) jvmSettings (
    jvmSharedSettings : _*
  ) jsSettings(
    jsSharedSettings : _*
  ) settings (
    description := "Scala.meta's quasiquotes for abstract syntax trees",
    enableHardcoreMacros
  ) dependsOn (parsers)

lazy val quasiquotesJS = quasiquotes.js
lazy val quasiquotesJVM = quasiquotes.jvm

lazy val scalameta = crossProject.in(file("scalameta/scalameta")
  ) settings (
    name := "scalameta"
  ) jvmSettings (
    jvmSharedSettings : _*
  ) jsSettings(
    jsSharedSettings : _*
  ) settings (
    description := "Scala.meta's metaprogramming APIs",
    enableMacros
  ) dependsOn (inline, tokenizers, transversers, quasiquotes)

lazy val scalametaJS = scalameta.js
lazy val scalametaJVM = scalameta.jvm

lazy val sharedSettings = Seq(
  scalaVersion := "2.11.8",
  version := "1.0-WHATEVER",
  organization := "org.scalameta",
  resolvers += Resolver.sonatypeRepo("snapshots"),
  resolvers += Resolver.sonatypeRepo("releases"),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked"),
  scalacOptions ++= Seq("-Xfatal-warnings"),
  parallelExecution in Test := false // hello, reflection sync!!
)

lazy val jsSharedSettings = sharedSettings ++ Seq(
  scalaJSUseRhino in Global := false,
  libraryDependencies ++= Seq(
    "org.scalatest" %%% "scalatest" % "3.0.0" % "test",
    "org.scalacheck" %%% "scalacheck" % "1.13.2" % "test"
  )
)

lazy val jvmSharedSettings = sharedSettings ++ Seq(
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "org.scalacheck" %% "scalacheck" % "1.13.2" % "test"
  )
)

def macroDependencies(hardcore: Boolean) = libraryDependencies ++= {
  val scalaReflect = Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value % "provided")
  val scalaCompiler = {
    if (hardcore) Seq("org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided")
    else Nil
  }
  val backwardCompat210 = {
    if (scalaVersion.value.startsWith("2.10")) Seq("org.scalamacros" %% "quasiquotes" % "2.1.0")
    else Seq()
  }
  scalaReflect ++ scalaCompiler ++ backwardCompat210
}

lazy val enableMacros = macroDependencies(hardcore = false)
lazy val enableHardcoreMacros = macroDependencies(hardcore = true)
