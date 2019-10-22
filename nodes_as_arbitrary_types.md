---
layout: scala mdocorial
title: "Decoding nodes as arbitrary types"
section: scala mdocorial
sort_order: 4
---

We've seen in a previous scala mdocorial how to extract [primitive types](nodes_as_primitive.html),
[tuples](nodes_as_tuples.html) and [case classes](nodes_as_case_classes.html) from XML documents. Sometimes however,
none of these fit our requirements. kantan.xpath provides support for extracting arbitrary types, which works almost
exactly the same as case classes.

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

We'll be trying to turn each `element` node into values of the following type:

```scala
class El(val id: Int, val enabled: Boolean) {
  override def toString = s"El($id, $enabled)"
}
```

This is done as usual, by declaring an  implicit [`NodeDecoder[El]`][`NodeDecoder`] value. We'll be using the same
[`decoder`] method as for [case classes](nodes_as_case_classes.html), but we don't have a convenient, pre-existing
instance creation function to provide as a parameter and will need to write it ourselves:

```scala
import kantan.xpath._
import kantan.xpath.implicits._

implicit val elDecoder: NodeDecoder[El] = NodeDecoder.decoder(xp"./@id", xp"./@enabled") { (id: Int, enabled: Boolean) =>
  new El(id, enabled)
}
```

Now that we have told kantan.xpath how to decode an XML node to an instance of `El`, we can simply call
[`evalXPath`] with the right type parameters:

```scala
rawData.evalXPath[List[El]](xp"//element")
// res1: XPathResult[List[El]] = Right(
//   List(El(1, true), El(2, false), El(3, true), El(4, false))
// )
```

[`NodeDecoder`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html
[`decoder`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html#decoder[I1,I2,O](x1:kantan.xpath.Query[kantan.xpath.DecodeResult[I1]],x2:kantan.xpath.Query[kantan.xpath.DecodeResult[I2]])(f:(I1,I2)=>O):kantan.xpath.NodeDecoder[O]
[`CompileResult`]:{{ site.baseurl }}/api/kantan/xpath/CompileResult$.html
[`get`]:https://nrinaudo.github.io/kantan.codecs/api/kantan/codecs/Result.html#get:S
[`evalXPath`]:{{ site.baseurl }}/api/kantan/xpath/ops/XmlSourceOps.html#evalXPath[B](expr:kantan.xpath.XPathExpression)(implicitevidence$2:kantan.xpath.Compiler[B],implicitsource:kantan.xpath.XmlSource[A]):kantan.xpath.XPathResult[B]
