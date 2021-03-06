---
layout: tutorial
title: "Decoding nodes as primitive types"
section: tutorial
sort_order: 1
---
The simplest possible use of kantan.xpath is to extract primitive types from XML documents.

In order to show how that works, we'll first need some sample XML data, which we'll get from this project's resources:

```scala mdoc:silent
val rawData: java.net.URL = getClass.getResource("/simple.xml")
```

This is what we're working with:

```scala mdoc
scala.io.Source.fromURL(rawData).mkString
```

We'll then need to import kantan.xpath's syntax, which will let us evaluate XPath expressions directly on something
that can be turned into an XML document:

```scala mdoc:silent
import kantan.xpath.implicits._
```

This allows us to write the following code, which will attempt to extract the `id` field of any `element` node as an
`Int`:

```scala mdoc
rawData.evalXPath[Int](xp"//element/@id")
```


There are a few things worth pointing out here. First, the return type: you might expect an `Int`, since this is what
you requested from [`evalXPath`], but we got an [`XPathResult[Int]`][`XPathResult`] instead. An [`XPathResult`] is
either a failure if something went wrong (the XPath expression is not valid, the `id` field is not a valid `Int`....) or
a success otherwise. This mechanism ensures that [`evalXPath`] is safe: no exception will be thrown and break the flow
of your code. For example:

```scala mdoc
rawData.evalXPath[java.net.URL](xp"//element/@id")
```

In some cases, however, we don't really care for runtime safety and are fine with our program crashing at the first
error. This is what the [`unsafeEvalXPath`] method was designed for:

```scala mdoc
rawData.unsafeEvalXPath[Int](xp"//element/@id")
```


Another point of interest is that the sample XML file contained multiple `element` nodes, but we only got the `id`
attribute of the first one. This is due to the type parameter we passed to [`evalXPath`]: by requesting a non-collection
type, we told kantan.xpath that we only wanted the first result. We could get them all by requesting a
[`List[Int]`][`List`], for example:


```scala mdoc
rawData.evalXPath[List[Int]](xp"//element/@id")
```

Any type constructor that has a [`CanBuildFrom`] instance could have been used instead of [`List`] - that's essentially
all collections. By the same token, any primitive time could have been used instead of `Int`. For example:

```scala mdoc
rawData.evalXPath[Vector[Boolean]](xp"//element/@enabled")
```

[`evalXPath`]:{{ site.baseurl }}/api/kantan/xpath/ops/XmlSourceOps.html#evalXPath[B](expr:kantan.xpath.XPathExpression)(implicitevidence$2:kantan.xpath.Compiler[B],implicitsource:kantan.xpath.XmlSource[A]):kantan.xpath.XPathResult[B]
[`unsafeEvalXPath`]:{{ site.baseurl }}/api/kantan/xpath/ops/XmlSourceOps.html#unsafeEvalXPath[B](expr:kantan.xpath.XPathExpression)(implicitevidence$1:kantan.xpath.Compiler[B],implicitsource:kantan.xpath.XmlSource[A]):B
[`XPathResult`]:{{ site.baseurl }}/api/kantan/xpath/XPathResult$.html
[`CanBuildFrom`]:http://www.scala-lang.org/api/current/scala/collection/generic/CanBuildFrom.html
[`List`]:http://www.scala-lang.org/api/current/scala/collection/immutable/List.html
