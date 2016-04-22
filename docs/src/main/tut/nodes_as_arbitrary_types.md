---
layout: tutorial
title: "Decoding nodes as arbitrary types"
section: tutorial
sort: 4
---
We've seen in a previous tutorial how to extract [primitive types](nodes_as_primitive.html),
[tuples](nodes_as_tuples.html) and [case classes](nodes_as_case_classes.html) from XML documents. Sometimes however,
none of these fit your requirements. kantan.xpath provides support for extracting arbitrary types, which works almost
exactly like case classes do.

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
class El(val id: Int, val enabled: Boolean) {
  override def toString = s"El($id, $enabled)"
}
```

This is done as usual, by declaring an  implicit [`NodeDecoder[El]`][`NodeDecoder`] value. We'll be using the same
[`decoder`] method as for case classes, but we don't have a convenient, pre-existing instance creation function to
provide as a parameter and will need to write it ourselves:

```tut:silent
import kantan.xpath._
import kantan.xpath.ops._

implicit val elDecoder = NodeDecoder.decoder("./@id", "./@enabled") { (id: Int, enabled: Boolean) ⇒
  new El(id, enabled)
}.get
```

It's worth pointing out that we had to call [`get`] on the result of that call: [`decoder`] might fail if one of the
specified XPath expressions is not legal, and its result is wrapped in a [`CompileResult`].

Now that we have told kantan.xpath how to decode an XML node to an instance of `El`, we can simply call
[`evalXPath`] with the right type parameters:

```tut
rawData.evalXPath[List[El]]("//element")
```

[`NodeDecoder`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.package@NodeDecoder[A]=kantan.codecs.Decoder[kantan.xpath.package.Node,A,kantan.xpath.DecodeError,kantan.xpath.codecs.type]
[`decoder`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.NodeDecoder$@decoder[I1,I2,O](x1:kantan.xpath.Expression[kantan.xpath.DecodeResult[I1]],x2:kantan.xpath.Expression[kantan.xpath.DecodeResult[I2]])(f:(I1,I2)=>O):kantan.xpath.NodeDecoder[O]
[`CompileResult`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.package@CompileResult[A]=kantan.codecs.Result[kantan.xpath.CompileError,A]
[`get`]:https://nrinaudo.github.io/kantan.codecs/api/index.html#kantan.codecs.Result@get:S
[`evalXPath`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.ops$$XmlSourceOps@evalXPath[B](expr:String)(implicitevidence$2:kantan.xpath.Compiler[B],implicitsource:kantan.xpath.XmlSource[A]):kantan.xpath.XPathResult[B]