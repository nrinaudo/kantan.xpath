import sbtunidoc.Plugin.UnidocKeys._



// - Dependency versions -----------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
val contextualVersion    = "1.0.0"
val kantanCodecsVersion  = "0.1.12-SNAPSHOT"
val nekoHtmlVersion      = "1.9.22"
val scalatestVersion     = "3.0.1"

kantanProject in ThisBuild := "xpath"



// - root projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val root = Project(id = "kantan-xpath", base = file("."))
  .settings(moduleName := "root")
  .enablePlugins(UnpublishedPlugin)
  .settings(
    initialCommands in console :=
    """
      |import kantan.xpath._
      |import kantan.xpath.implicits._
      |import kantan.xpath.joda.time._
    """.stripMargin
  )
  .aggregate(core, nekohtml, docs, laws, cats, scalaz, jodaTime)
  .aggregate(ifJava8[ProjectReference](java8):_*)
  .dependsOn(core, nekohtml, jodaTime)

lazy val docs = project
  .settings(unidocProjectFilter in (ScalaUnidoc, unidoc) :=
    inAnyProject -- inProjects(ifNotJava8[ProjectReference](java8):_*)
  )
  .enablePlugins(DocumentationPlugin)
  .dependsOn(core, nekohtml, cats, scalaz, jodaTime)
  .dependsOn(ifJava8[ClasspathDep[ProjectReference]](java8):_*)



// - core projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val core = project
  .settings(
    moduleName := "kantan.xpath",
    name       := "core"
  )
  .enablePlugins(PublishedPlugin)
  .enablePlugins(spray.boilerplate.BoilerplatePlugin)
  .settings(libraryDependencies ++= Seq(
    "com.propensive" %% "contextual"    % contextualVersion,
    "com.nrinaudo"   %% "kantan.codecs" % kantanCodecsVersion,
    "org.scalatest"  %% "scalatest"     % scalatestVersion    % "test"
  ))
  .laws("laws")

lazy val laws = project
  .settings(
    moduleName := "kantan.xpath-laws",
    name       := "laws"
  )
  .enablePlugins(PublishedPlugin)
  .enablePlugins(spray.boilerplate.BoilerplatePlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs-laws" % kantanCodecsVersion)



// - joda-time projects ------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val jodaTime = Project(id = "joda-time", base = file("joda-time"))
  .settings(
    moduleName := "kantan.xpath-joda-time",
    name       := "joda-time"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % "test")
  .settings(libraryDependencies ++= Seq(
    "com.nrinaudo"  %% "kantan.codecs-joda-time"      % kantanCodecsVersion,
    "com.nrinaudo"  %% "kantan.codecs-joda-time-laws" % kantanCodecsVersion % "test",
    "org.scalatest" %% "scalatest"                    % scalatestVersion    % "test"
  ))



// - java8 projects ----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val java8 = project
  .settings(
    moduleName    := "kantan.xpath-java8",
    name          := "java8"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % "test")
  .settings(libraryDependencies ++= Seq(
    "com.nrinaudo"  %% "kantan.codecs-java8"      % kantanCodecsVersion,
    "com.nrinaudo"  %% "kantan.codecs-java8-laws" % kantanCodecsVersion % "test",
    "org.scalatest" %% "scalatest"                % scalatestVersion    % "test"
  ))



// - external parsers projects -----------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val nekohtml = project
  .settings(
    moduleName := "kantan.xpath-nekohtml",
    name       := "nekohtml"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core)
  .settings(libraryDependencies ++= Seq(
    "net.sourceforge.nekohtml" %  "nekohtml"  % nekoHtmlVersion,
    "org.scalatest"            %% "scalatest" % scalatestVersion % "test"
  ))



// - cats projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val cats = project
  .settings(
    moduleName := "kantan.xpath-cats",
    name       := "cats"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % "test")
  .settings(libraryDependencies ++= Seq(
    "com.nrinaudo"  %% "kantan.codecs-cats"      % kantanCodecsVersion,
    "com.nrinaudo"  %% "kantan.codecs-cats-laws" % kantanCodecsVersion % "test",
    "org.scalatest" %% "scalatest"               % scalatestVersion    % "test"
  ))



// - scalaz projects ---------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val scalaz = project
  .settings(
    moduleName := "kantan.xpath-scalaz",
    name       := "scalaz"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % "test")
  .settings(libraryDependencies ++= Seq(
    "com.nrinaudo"  %% "kantan.codecs-scalaz"      % kantanCodecsVersion,
    "com.nrinaudo"  %% "kantan.codecs-scalaz-laws" % kantanCodecsVersion % "test",
    "org.scalatest" %% "scalatest"                 % scalatestVersion    % "test"
  ))
