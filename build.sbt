ThisBuild / kantanProject := "xpath"
ThisBuild / startYear     := Some(2016)

// - root projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val root = Project(id = "kantan-xpath", base = file("."))
  .settings(moduleName := "root")
  .enablePlugins(UnpublishedPlugin)
  .settings(
    console / initialCommands :=
      """
      |import kantan.xpath._
      |import kantan.xpath.implicits._
      |import kantan.xpath.refined._
    """.stripMargin
  )
  .aggregate(cats, core, docs, enumeratum, java8, laws, libra, nekohtml, refined, scalaz)
  .dependsOn(core, libra, nekohtml, refined)

lazy val docs = project
  .settings(
    ScalaUnidoc / unidoc / unidocProjectFilter :=
      inAnyProject
  )
  .enablePlugins(DocumentationPlugin)
  .settings(name := "docs")
  .dependsOn(cats, core, enumeratum, java8, libra, nekohtml, refined, scalaz)

// - core projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val core = project
  .settings(
    moduleName := "kantan.xpath",
    name       := "core"
  )
  .enablePlugins(PublishedPlugin, spray.boilerplate.BoilerplatePlugin)
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %% "kantan.codecs" % Versions.kantanCodecs
    )
  )
  .laws("laws")

lazy val laws = project
  .settings(
    moduleName := "kantan.xpath-laws",
    name       := "laws"
  )
  .enablePlugins(PublishedPlugin, spray.boilerplate.BoilerplatePlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs-laws" % Versions.kantanCodecs)

// - java8 projects ----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val java8 = project
  .settings(
    moduleName := "kantan.xpath-java8",
    name       := "java8"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % Test)
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %% "kantan.codecs-java8"      % Versions.kantanCodecs,
      "com.nrinaudo" %% "kantan.codecs-java8-laws" % Versions.kantanCodecs % Test
    )
  )

// - external parsers projects -----------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val nekohtml = project
  .settings(
    moduleName := "kantan.xpath-nekohtml",
    name       := "nekohtml"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % Test)
  .settings(
    libraryDependencies ++= Seq(
      "net.sourceforge.nekohtml" % "nekohtml" % Versions.nekoHtml
    )
  )

// - cats projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val cats = project
  .settings(
    moduleName := "kantan.xpath-cats",
    name       := "cats"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % Test)
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %% "kantan.codecs-cats"      % Versions.kantanCodecs,
      "com.nrinaudo" %% "kantan.codecs-cats-laws" % Versions.kantanCodecs % Test
    )
  )

// - scalaz projects ---------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val scalaz = project
  .settings(
    moduleName := "kantan.xpath-scalaz",
    name       := "scalaz"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % Test)
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %% "kantan.codecs-scalaz"      % Versions.kantanCodecs,
      "com.nrinaudo" %% "kantan.codecs-scalaz-laws" % Versions.kantanCodecs % Test
    )
  )

// - refined project ---------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val refined = project
  .settings(
    moduleName := "kantan.xpath-refined",
    name       := "refined"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % Test)
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %% "kantan.codecs-refined"      % Versions.kantanCodecs,
      "com.nrinaudo" %% "kantan.codecs-refined-laws" % Versions.kantanCodecs % Test
    )
  )

// - Enumeratum project ------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val enumeratum = project
  .settings(
    moduleName := "kantan.xpath-enumeratum",
    name       := "enumeratum"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % Test)
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %% "kantan.codecs-enumeratum"      % Versions.kantanCodecs,
      "com.nrinaudo" %% "kantan.codecs-enumeratum-laws" % Versions.kantanCodecs % Test
    )
  )

// - Libra project -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val libra = project
  .settings(
    moduleName := "kantan.xpath-libra",
    name       := "libra"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % Test)
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %% "kantan.codecs-libra"      % Versions.kantanCodecs,
      "com.nrinaudo" %% "kantan.codecs-libra-laws" % Versions.kantanCodecs % Test
    )
  )
