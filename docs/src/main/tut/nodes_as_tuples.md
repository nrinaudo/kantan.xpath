---
layout: tutorial
title: "Decoding nodes as tuples"
section: tutorial
sort: 2
---


```tut:silent
val rawData: java.net.URL = getClass.getResource("/simple.xml")
```

```tut
scala.io.Source.fromURL(rawData).mkString
```

```tut:silent
import kantan.xpath._
import kantan.xpath.ops._

implicit val elementDecoder = NodeDecoder.tuple[Int, Boolean]("./@id", "./@enabled").get
```

```tut
rawData.evalXPath[List[(Int, Boolean)]]("//element")
```