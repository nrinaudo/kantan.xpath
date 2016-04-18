---
layout: tutorial
title: "Decoding nodes as case classes"
section: tutorial
sort: 3
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

case class Element(id: Int, enabled: Boolean)

implicit val elementDecoder = NodeDecoder.decoder("./@id", "./@enabled")(Element).get
```

```tut
rawData.evalXPath[List[Element]]("//element")
```