---
layout: tutorial
title: "Decoding nodes as arbitrary types"
section: tutorial
sort: 4
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

class Element(val id: Int, val enabled: Boolean) {
  override def toString = s"Element($id, $enabled)"
}

implicit val elementDecoder = NodeDecoder.decoder("./@id", "./@enabled") { (id: Int, enabled: Boolean) ⇒
  new Element(id, enabled)
}.get
```

```tut
rawData.evalXPath[List[Element]]("//element")
```