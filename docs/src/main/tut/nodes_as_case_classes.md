---
layout: tutorial
title: "Decoding nodes as case classes"
section: tutorial
sort_order: 3
---
We've seen in a [previous tutorial](nodes_as_tuples.html) how to extract tuples from XML documents. The next step up
from tuples is case classes, which work in a very similar fashion.

In order to show how that works, we'll first need some sample XML data, which we'll get from this project's resources:

```tut:silent
val rawData: java.net.URL = getClass.getResource("/simple.xml")
```

This is what we're working with:

```tut
scala.io.Source.fromURL(rawData).mkString
```

We'll be trying to turn each `element` node into values of the following type:

```tut:silent
case class El(id: Int, enabled: Boolean)
```

In the same way that we had to declare a [`NodeCoder[(Int, Boolean)]`][`NodeDecoder`] to decode tuples, we'll need a
[`NodeDecoder[El]`][`NodeDecoder`] for this case class, which we can easily create through the [`decoder`] method:


```tut:silent
import kantan.xpath._
import kantan.xpath.implicits._

// There is no need to specify type parameters here, the Scala compiler works them out from El.apply.
implicit val elDecoder = NodeDecoder.decoder(xp"./@id", xp"./@enabled")(El.apply)
```

Now that we have told kantan.xpath how to decode an XML node to an instance of `El`, we can simply call
[`evalXPath`] with the right type parameters:

```tut
rawData.evalXPath[List[El]](xp"//element")
```

[`NodeDecoder`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.package@NodeDecoder[A]=kantan.codecs.Decoder[kantan.xpath.package.Node,A,kantan.xpath.DecodeError,kantan.xpath.codecs.type]
[`decoder`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.NodeDecoder$@decoder[I1,I2,O](x1:kantan.xpath.Query[kantan.xpath.DecodeResult[I1]],x2:kantan.xpath.Query[kantan.xpath.DecodeResult[I2]])(f:(I1,I2)=>O):kantan.xpath.NodeDecoder[O]
[`CompileResult`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.package@CompileResult[A]=kantan.codecs.Result[kantan.xpath.CompileError,A]
[`get`]:https://nrinaudo.github.io/kantan.codecs/api/index.html#kantan.codecs.Result@get:S
[`evalXPath`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.ops$$XmlSourceOps@evalXPath[B](expr:String)(implicitevidence$2:kantan.xpath.Compiler[B],implicitsource:kantan.xpath.XmlSource[A]):kantan.xpath.XPathResult[B]
