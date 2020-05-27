---
layout: tutorial
title: "Refined module"
section: tutorial
sort_order: 14
---
kantan.xpath comes with a [refined](https://github.com/fthomas/refined) module that can be used
by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-refined" % "0.5.2"
```

You then need to import the corresponding package:

```scala
import kantan.xpath.refined._
```

And that's pretty much it. You can now decode refined types directly.

Let's first set our types up:

```scala
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive
import kantan.xpath.implicits._

type PositiveInt = Int Refined Positive
```

We can then simply write the following:

```scala
"<foo><bar value='1'/></foo>".evalXPath[PositiveInt](xp"//bar/@value")
// res0: kantan.xpath.package.XPathResult[PositiveInt] = Right(1)
```

And, for an error case:

```scala
"<foo><bar value='-1'/></foo>".evalXPath[PositiveInt](xp"//bar/@value")
// res1: kantan.xpath.package.XPathResult[PositiveInt] = Left(
//   TypeError("Not acceptable: 'Predicate failed: (-1 > 0).'")
// )
```
