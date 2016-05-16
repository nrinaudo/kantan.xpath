---
layout: tutorial
title: "Compiling queries for reuse"
section: tutorial
sort: 6
---
In the examples we've seen so far, XPath expressions were passed around as strings. This can be inefficient, since they
must be recompiled each time they're evaluated against a document.

When working with expressions that will need to be applied over and over, it's more efficient to compile them as
instances of [`Query`].

In order to show how that works, we'll need some sample XML data, which we'll get from this project's resources:

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

We'll be trying to extract the `id` attribute of each `element` node as an `int`. Compiling the corresponding XPath
expression is done through [`compile`]:

```scala
import kantan.xpath._
import kantan.xpath.ops._

val query = Query.compile[List[Int]]("//element/@id").get
```

Note that we called [`get`] on the return value: compiling an XPath expression might fail if the expression is invalid,
and [`compile`] wraps its return value in a [`CompileResult`]. It's usually better to deal with errors rather than
let them turn into runtime exceptions, but if you don't mind them, you can also use the [`xpath`] method that enriches
strings:

```scala
val query = "//element/@id".xpath[List[Int]]
```

You can now use the compiled query where you used to specify strings, such as in [`evalXPath`]:

```scala
scala> rawData.evalXPath(query)
res2: kantan.xpath.ReadResult[List[Int]] = Success(List(1, 2, 3, 4))
```

Note that since compiled queries carry the information of the type they return, you don't need to specify type
parameters to [`evalXPath`].

[`Query`]:{{ site.baseUrl }}/api/#kantan.xpath.Query
[`compile`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.Query$@compile[A](str:String)(implicitcmp:kantan.xpath.Compiler[A]):kantan.xpath.XPathResult[kantan.xpath.Query[kantan.xpath.DecodeResult[A]]]
[`get`]:https://nrinaudo.github.io/kantan.codecs/api/index.html#kantan.codecs.Result@get:S
[`CompileResult`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.package@CompileResult[A]=kantan.codecs.Result[kantan.xpath.CompileError,A]
[`xpath`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.ops$$StringOps@xpath[A](implicitcomp:kantan.xpath.Compiler[A]):kantan.xpath.Query[kantan.xpath.DecodeResult[A]]
[`evalXPath`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.ops$$XmlSourceOps@evalXPath[B](expr:kantan.xpath.Query[kantan.xpath.DecodeResult[B]])(implicitsource:kantan.xpath.XmlSource[A]):kantan.xpath.ReadResult[B]
