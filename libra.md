---
layout: tutorial
title: "Libra module"
section: tutorial
sort_order: 16
---

kantan.xpath comes with a [libra](https://github.com/to-ithaca/libra) module that can be used
by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-libra" % "0.5.2-SNAPSHOT"
```

You then need to import the corresponding package:

```scala
import kantan.xpath.libra._
```

And that's pretty much it. You can now decode refined types directly.

Let's first set our types up:

```scala
import libra._
import kantan.xpath.implicits._

type Duration = QuantityOf[Int, Time, Second]
```

We can then simply write the following:

```scala
"<foo><bar duration='1'/></foo>".evalXPath[Duration](xp"//bar/@duration")
// res0: kantan.xpath.package.XPathResult[Duration] = Right(Quantity(1))
```

