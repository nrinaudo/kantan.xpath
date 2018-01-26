---
layout: tutorial
title: "Libra module"
section: tutorial
sort_order: 16
---
kantan.xpath comes with a [libra](https://github.com/to-ithaca/libra) module that can be used
by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-libra" % "@VERSION@"
```

You then need to import the corresponding package:

```tut:silent
import kantan.xpath.libra._
```

And that's pretty much it. You can now decode refined types directly.

Let's first set our types up:

```tut:silent
import libra._
import libra.si._
import kantan.xpath.implicits._

type Duration = QuantityOf[Int, Time, Second]
```

We can then simply write the following:

```tut
"<foo><bar duration='1'/></foo>".evalXPath[Duration](xp"//bar/@duration")
```
