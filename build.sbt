import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import com.typesafe.sbt.SbtSite.SiteKeys._
import UnidocKeys._

val simulacrumVersion    = "0.7.0"
val macroParadiseVersion = "2.1.0"
val nekoHtmlVersion      = "1.9.22"
val scalatestVersion     = "3.0.0-M9"
val scalaCheckVersion    = "1.12.5"
val disciplineVersion    = "0.4"

lazy val buildSettings = Seq(
  organization       := "com.nrinaudo",
  scalaVersion       := "2.11.7",
  crossScalaVersions := Seq("2.10.6", "2.11.7")
)

lazy val compilerOptions = Seq("-deprecation",
  "-target:jvm-1.7",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture")

lazy val baseSettings = Seq(
  scalacOptions ++= compilerOptions,
  libraryDependencies ++= Seq(
    "com.github.mpilquist" %% "simulacrum"    % simulacrumVersion % "provided",
    compilerPlugin("org.scalamacros" % "paradise" % macroParadiseVersion cross CrossVersion.full)
  ),
  coverageExcludedPackages := "grind\\.laws\\..*",
  incOptions     := incOptions.value.withNameHashing(true)
)

lazy val noPublishSettings = Seq(
  publish         := (),
  publishLocal    := (),
  publishArtifact := false
)

lazy val publishSettings = Seq(
  homepage := Some(url("https://nrinaudo.github.io/grind")),
  licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")),
  apiURL := Some(url("https://nrinaudo.github.io/grind/api/")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/nrinaudo/grind"),
      "scm:git:git@github.com:nrinaudo/grind.git"
    )
  ),
  pomExtra := <developers>
    <developer>
      <id>nrinaudo</id>
      <name>Nicolas Rinaudo</name>
      <url>http://nrinaudo.github.io</url>
    </developer>
  </developers>
)


lazy val allSettings = buildSettings ++ baseSettings ++ publishSettings

lazy val root = Project(id = "grind", base = file("."))
  .settings(moduleName := "root")
  .settings(allSettings)
  .settings(noPublishSettings)
  .aggregate(core, nekohtml, docs, laws, tests)
  .dependsOn(core, nekohtml)

lazy val core = project
  .settings(
    moduleName := "grind",
    name       := "core"
  )
  .settings(sourceGenerators in Compile <+= (sourceManaged in Compile).map(Boilerplate.gen))
  .settings(allSettings: _*)

lazy val nekohtml = project
  .settings(libraryDependencies += "net.sourceforge.nekohtml" % "nekohtml" % nekoHtmlVersion)
  .settings(allSettings: _*)
  .dependsOn(core)

lazy val laws = project
  .settings(
    moduleName := "grind-laws",
    name       := "laws"
  )
  .settings(libraryDependencies ++= Seq(
    "org.scalacheck" %% "scalacheck" % scalaCheckVersion,
    "org.typelevel"  %% "discipline" % disciplineVersion
  ))
  .settings(allSettings: _*)
  .dependsOn(core)

lazy val tests = project
  .settings(allSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % "test")
  .dependsOn(core, laws % "test")


lazy val docs = project
  .settings(allSettings: _*)
  .settings(site.settings: _*)
  .settings(ghpages.settings: _*)
  .settings(unidocSettings: _*)
  .settings(
    autoAPIMappings := true,
    apiURL := Some(url("http://nrinaudo.github.io/grind/api/")),
    scalacOptions in (ScalaUnidoc, unidoc) ++= Seq(
      "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
      "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
    )
  )
  .settings(tutSettings: _*)
  .settings(
    site.addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), "api"),
    site.addMappingsToSiteDir(tut, "_tut"),
    git.remoteRepo := "git@github.com:nrinaudo/grind.git",
    ghpagesNoJekyll := false,
    includeFilter in makeSite := "*.yml" | "*.md" | "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" |
                                 "*.eot" | "*.svg" | "*.ttf" | "*.woff" | "*.woff2" | "*.otf"
  )
  .settings(noPublishSettings:_*)
  .dependsOn(core, nekohtml)