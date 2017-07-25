---
layout: tutorial
title: "Compiling queries for reuse"
section: tutorial
sort_order: 6
---
In the examples we've seen so far, XPath expressions were passed around as [`XPathExpression`]s. This can be
inefficient as kantan.xpath needs to bake in the decoding code each time they're evaluated against a node.

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

We'll be trying to extract the `id` attribute of each `element` node as an `int`.


## Compiling XPath literals

Compiling XPath literals is done through [`Query.apply`]:

```scala
import kantan.xpath._
import kantan.xpath.implicits._

val query = Query[List[Int]](xp"//element/@id")
```

You can now use the compiled query where you used to specify strings, such as in [`evalXPath`]:

```scala
scala> rawData.evalXPath(query)
res2: kantan.xpath.ReadResult[List[Int]] = Success(List(1, 2, 3, 4))
```

Note that since compiled queries carry the information of the type they return, you don't need to specify type
parameters to [`evalXPath`].


## Compiling strings

You cannot always express your XPath expressions as literals - some expressions are built dynamically, for instance.
You can use [`compile`] to compile raw strings:

```scala
scala> val query = Query.compile[List[Int]]("//element/@id")
query: kantan.xpath.CompileResult[kantan.xpath.Query[kantan.xpath.DecodeResult[List[Int]]]] = Success(kantan.xpath.Query$$anon$1@108bd94b)
```

The returned value is not directly a [`Query`], though, but rather a [`CompileResult`] containing an instance of
[`Query`]: there is no way to guarantee at compile time that the specified XPath expression is valid, and we must have
some error handling in place.


[`Query`]:{{ site.baseurl }}/api/kantan/xpath/Query.html
[`compile`]:{{ site.baseurl }}/api/kantan/xpath/Query$.html#compile[A](str:String)(implicitevidence$2:kantan.xpath.Compiler[A],implicitxpath:kantan.xpath.XPathCompiler):kantan.xpath.CompileResult[kantan.xpath.Query[kantan.xpath.DecodeResult[A]]]
[`get`]:https://nrinaudo.github.io/kantan.codecs/api/index.html#kantan.codecs.Result@get:S
[`CompileResult`]:{{ site.baseurl }}/api/kantan/xpath/CompileResult$.html
[`xpath`]:{{ site.baseurl }}/api/index.html#kantan.xpath.ops$$StringOps@xpath[A](implicitcomp:kantan.xpath.Compiler[A]):kantan.xpath.Query[kantan.xpath.DecodeResult[A]]
[`evalXPath`]:{{ site.baseurl }}/api/kantan/xpath/ops/XmlSourceOps.html#evalXPath[B](expr:kantan.xpath.XPathExpression)(implicitevidence$2:kantan.xpath.Compiler[B],implicitsource:kantan.xpath.XmlSource[A]):kantan.xpath.XPathResult[B]
[`Query.apply`]:{{ site.baseurl }}/api/kantan/xpath/Query$.html#apply[A](expr:kantan.xpath.XPathExpression)(implicitevidence$1:kantan.xpath.Compiler[A]):kantan.xpath.Query[kantan.xpath.DecodeResult[A]]
[`XPathExpression`]:{{ site.baseurl }}/api/kantan/xpath/index.html#XPathExpression=javax.xml.xpath.XPathExpression
