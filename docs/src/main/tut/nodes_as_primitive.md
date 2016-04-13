---
layout: tutorial
title: "Decoding nodes as primitive types"
section: tutorial
sort: 1
status: wip
---
The problem kantan.xpath attempts to solve is extracting useful types from the result of XPath expressions. 
The simplest possible 

```tut:silent
import kantan.xpath.ops._
```

```tut:silent
val rawData: java.net.URL = getClass.getResource("/simple.xml")
```

```tut
scala.io.Source.fromURL(rawData).mkString
```

```tut
rawData.first[Int]("root/element/@id".xpath)

rawData.all[List, Int]("root/element/@id".xpath)
```

```tut
rawData.first[Boolean]("root/element/@enabled".xpath)

rawData.all[List, Boolean]("root/element/@enabled".xpath)
```

```tut
rawData.first[Float]("root/element/@enabled".xpath)
```
