---
layout: scala mdocorial
title: "Decoding nodes as tuples"
section: scala mdocorial
sort_order: 2
---

We've seen in a [previous scala mdocorial](nodes_as_primitive.html) how to extract primitive types from XML documents. Often,
however, these primitive values need to be assembled in more complex types, such as tuples. kantan.xpath provides a
simple way of doing so.

In order to show how that works, we'll first need some sample XML data, which we'll get from this project's resources:

```scala
val rawData: java.net.URL = getClass.getResource("/simple.xml")
```

This is what we're working with:

```scala
scala.io.Source.fromURL(rawData).mkString
// res0: String = """<root>
//     <element id="1" enabled="true"/>
//     <element id="2" enabled="false"/>
//     <element id="3" enabled="true"/>
//     <element id="4" enabled="false"/>
// </root>"""
```

We'll be trying to turn each `element` node into an `(Int, Boolean)` tuple. In order to do that, we need to declare an
implicit [`NodeDecoder[(Int, Boolean)]`][`NodeDecoder`] value: this will be automatically picked up by kantan.xpath and
used when we request XML nodes to be interpreted as `(Int, Boolean)` values.

The easier way to declare a [`NodeDecoder`] instance for tuples is to use the dedicated [`tuple`] method, which takes
one XPath expression per field to extract:

```scala
import kantan.xpath._
import kantan.xpath.implicits._

implicit val elementDecoder: NodeDecoder[(Int, Boolean)] = NodeDecoder.tuple[Int, Boolean](xp"./@id", xp"./@enabled")
```

Now that we have told kantan.xpath how to decode an XML node to an instance of `(Int, Boolean)`, we can simply call
[`evalXPath`] with the right type parameters:

```scala
rawData.evalXPath[List[(Int, Boolean)]](xp"//element")
// res1: XPathResult[List[(Int, Boolean)]] = Right(
//   List((1, true), (2, false), (3, true), (4, false))
// )
```

[`NodeDecoder`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html
[`tuple`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html#tuple[I1,I2](x1:kantan.xpath.Query[kantan.xpath.DecodeResult[I1]],x2:kantan.xpath.Query[kantan.xpath.DecodeResult[I2]]):kantan.xpath.NodeDecoder[(I1,I2)]
[`CompileResult`]:{{ site.baseurl }}/api/kantan/xpath/CompileResult$.html
[`get`]:https://nrinaudo.github.io/kantan.codecs/api/kantan/codecs/Result.html#get:S
[`evalXPath`]:{{ site.baseurl }}/api/kantan/xpath/ops/XmlSourceOps.html#evalXPath[B](expr:kantan.xpath.XPathExpression)(implicitevidence$2:kantan.xpath.Compiler[B],implicitsource:kantan.xpath.XmlSource[A]):kantan.xpath.XPathResult[B]
