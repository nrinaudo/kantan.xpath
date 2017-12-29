---
layout: tutorial
title: "Decoding nodes as case classes"
section: tutorial
sort_order: 3
---
We've seen in a [previous tutorial](nodes_as_tuples.html) how to extract tuples from XML documents. The next step up
from tuples is case classes, which work in a very similar fashion.

In order to show how that works, we'll first need some sample XML data, which we'll get from this project's resources:

```scala
val rawData: java.net.URL = getClass.getResource("/simple.xml")
```

This is what we're working with:

```scala
scala> scala.io.Source.fromURL(rawData).mkString
res0: String =
<root>
    <element id="1" enabled="true"/>
    <element id="2" enabled="false"/>
    <element id="3" enabled="true"/>
    <element id="4" enabled="false"/>
</root>
```

We'll be trying to turn each `element` node into values of the following type:

```scala
final case class El(id: Int, enabled: Boolean)
```

In the same way that we had to declare a [`NodeCoder[(Int, Boolean)]`][`NodeDecoder`] to decode tuples, we'll need a
[`NodeDecoder[El]`][`NodeDecoder`] for this case class, which we can easily create through the [`decoder`] method:


```scala
import kantan.xpath._
import kantan.xpath.implicits._

// There is no need to specify type parameters here, the Scala compiler works them out from El.apply.
implicit val elDecoder: NodeDecoder[El] = NodeDecoder.decoder(xp"./@id", xp"./@enabled")(El.apply)
```

Now that we have told kantan.xpath how to decode an XML node to an instance of `El`, we can simply call
[`evalXPath`] with the right type parameters:

```scala
scala> rawData.evalXPath[List[El]](xp"//element")
<console>:13: warning: Unused import
       import kantan.xpath._
                           ^
res3: kantan.xpath.XPathResult[List[El]] = Success(List(El(1,true), El(2,false), El(3,true), El(4,false)))
```

[`NodeDecoder`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html
[`decoder`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html#decoder[I1,I2,O](x1:kantan.xpath.Query[kantan.xpath.DecodeResult[I1]],x2:kantan.xpath.Query[kantan.xpath.DecodeResult[I2]])(f:(I1,I2)=>O):kantan.xpath.NodeDecoder[O]
[`CompileResult`]:{{ site.baseurl }}/api/kantan/xpath/CompileResult$.html
[`get`]:https://nrinaudo.github.io/kantan.codecs/api/index.html#kantan.codecs.Result@get:S
[`evalXPath`]:{{ site.baseurl }}/api/kantan/xpath/ops/XmlSourceOps.html#evalXPath[B](expr:kantan.xpath.XPathExpression)(implicitevidence$2:kantan.xpath.Compiler[B],implicitsource:kantan.xpath.XmlSource[A]):kantan.xpath.XPathResult[B]
