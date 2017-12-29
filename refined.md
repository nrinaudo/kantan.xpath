---
layout: tutorial
title: "Refined module"
section: tutorial
sort_order: 14
---
kantan.xpath comes with a [refined](https://github.com/fthomas/refined) module that can be used
by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-refined" % "0.3.2"
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
scala> "<foo><bar value='1'/></foo>".evalXPath[PositiveInt](xp"//bar/@value")
<console>:12: warning: Unused import
       import eu.timepit.refined.api.Refined
                                     ^
<console>:13: warning: Unused import
       import eu.timepit.refined.numeric.Positive
                                         ^
res1: kantan.xpath.XPathResult[PositiveInt] = Success(1)
```

And, for an error case:

```scala
scala> "<foo><bar value='-1'/></foo>".evalXPath[PositiveInt](xp"//bar/@value")
<console>:12: warning: Unused import
       import eu.timepit.refined.api.Refined
                                     ^
<console>:13: warning: Unused import
       import eu.timepit.refined.numeric.Positive
                                         ^
res2: kantan.xpath.XPathResult[PositiveInt] = Failure(TypeError: Not acceptable: 'Predicate failed: (-1 > 0).')
```