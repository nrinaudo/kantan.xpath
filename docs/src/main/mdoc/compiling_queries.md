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

```scala mdoc:silent
val rawData: java.net.URL = getClass.getResource("/simple.xml")
```

This is what we're working with:

```scala mdoc
scala.io.Source.fromURL(rawData).mkString
```

We'll be trying to extract the `id` attribute of each `element` node as an `int`.


## Compiling XPath literals

Compiling XPath literals is done through [`Query.apply`]:

```scala mdoc:silent
import kantan.xpath._
import kantan.xpath.implicits._

val query = Query[List[Int]](xp"//element/@id")
```

You can now use the compiled query where you used to specify strings, such as in [`evalXPath`]:

```scala mdoc
rawData.evalXPath(query)
```

Note that since compiled queries carry the information of the type they return, you don't need to specify type
parameters to [`evalXPath`].


## Compiling strings

You cannot always express your XPath expressions as literals - some expressions are built dynamically, for instance.
You can use [`compile`] to compile raw strings:

```scala mdoc
Query.compile[List[Int]]("//element/@id")
```

The returned value is not directly a [`Query`], though, but rather a [`CompileResult`] containing an instance of
[`Query`]: there is no way to guarantee at compile time that the specified XPath expression is valid, and we must have
some error handling in place.


[`Query`]:{{ site.baseurl }}/api/kantan/xpath/Query.html
[`compile`]:{{ site.baseurl }}/api/kantan/xpath/Query$.html#compile[A](str:String)(implicitevidence$2:kantan.xpath.Compiler[A],implicitxpath:kantan.xpath.XPathCompiler):kantan.xpath.CompileResult[kantan.xpath.Query[kantan.xpath.DecodeResult[A]]]
[`get`]:https://nrinaudo.github.io/kantan.codecs/api/kantan/codecs/Result.html#get:S
[`CompileResult`]:{{ site.baseurl }}/api/kantan/xpath/CompileResult$.html
[`evalXPath`]:{{ site.baseurl }}/api/kantan/xpath/ops/XmlSourceOps.html#evalXPath[B](expr:kantan.xpath.XPathExpression)(implicitevidence$2:kantan.xpath.Compiler[B],implicitsource:kantan.xpath.XmlSource[A]):kantan.xpath.XPathResult[B]
[`Query.apply`]:{{ site.baseurl }}/api/kantan/xpath/Query$.html#apply[A](expr:kantan.xpath.XPathExpression)(implicitevidence$1:kantan.xpath.Compiler[A]):kantan.xpath.Query[kantan.xpath.DecodeResult[A]]
[`XPathExpression`]:{{ site.baseurl }}/api/kantan/xpath/index.html#XPathExpression=javax.xml.xpath.XPathExpression
