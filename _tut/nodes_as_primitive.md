---
layout: tutorial
title: "Decoding nodes as primitive types"
section: tutorial
sort_order: 1
---
The simplest possible use of kantan.xpath is to extract primitive types from XML documents.

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

We'll then need to import kantan.xpath's syntax, which will let us evaluate XPath expressions directly on something
that can be turned into an XML document:

```scala
import kantan.xpath.implicits._
```

This allows us to write the following code, which will attempt to extract the `id` field of any `element` node as an
`Int`:

```scala
scala> rawData.evalXPath[Int](xp"//element/@id")
res1: kantan.xpath.XPathResult[Int] = Success(1)
```


There are a few things worth pointing out here. First, the return type: you might expect an `Int`, since this is what
you requested from [`evalXPath`], but we got an [`XPathResult[Int]`][`XPathResult`] instead. An [`XPathResult`] is
either a failure if something went wrong (the XPath expression is not valid, the `id` field is not a valid `Int`....) or
a success otherwise. This mechanism ensures that [`evalXPath`] is safe: no exception will be thrown and break the flow
of your code. For example:

```scala
scala> rawData.evalXPath[java.net.URL](xp"//element/@id")
res2: kantan.xpath.XPathResult[java.net.URL] = Failure(kantan.xpath.DecodeError$TypeError$$anon$1: Not a valid URL: '1')
```

In some cases, however, we don't really care for runtime safety and are fine with our program crashing at the first
error. This is what the [`unsafeEvalXPath`] method was designed for:

```scala
scala> rawData.unsafeEvalXPath[Int](xp"//element/@id")
res3: Int = 1
```


Another point of interest is that the sample XML file contained multiple `element` nodes, but we only got the `id`
attribute of the first one. This is due to the type parameter we passed to [`evalXPath`]: by requesting a non-collection
type, we told kantan.xpath that we only wanted the first result. We could get them all by requesting a
[`List[Int]`][`List`], for example:


```scala
scala> rawData.evalXPath[List[Int]](xp"//element/@id")
res4: kantan.xpath.XPathResult[List[Int]] = Success(List(1, 2, 3, 4))
```

Any type constructor that has a [`CanBuildFrom`] instance could have been used instead of [`List`] - that's essentially
all collections. By the same token, any primitive time could have been used instead of `Int`. For example:

```scala
scala> rawData.evalXPath[Vector[Boolean]](xp"//element/@enabled")
res5: kantan.xpath.XPathResult[Vector[Boolean]] = Success(Vector(true, false, true, false))
```

[`evalXPath`]:{{ site.baseurl }}/api/kantan/xpath/ops/XmlSourceOps.html#evalXPath[B](expr:kantan.xpath.XPathExpression)(implicitevidence$2:kantan.xpath.Compiler[B],implicitsource:kantan.xpath.XmlSource[A]):kantan.xpath.XPathResult[B]
[`unsafeEvalXPath`]:{{ site.baseurl }}/api/kantan/xpath/ops/XmlSourceOps.html#unsafeEvalXPath[B](expr:kantan.xpath.XPathExpression)(implicitevidence$1:kantan.xpath.Compiler[B],implicitsource:kantan.xpath.XmlSource[A]):B
[`XPathResult`]:{{ site.baseurl }}/api/kantan/xpath/XPathResult$.html
[`CanBuildFrom`]:http://www.scala-lang.org/api/current/scala/collection/generic/CanBuildFrom.html
[`List`]:http://www.scala-lang.org/api/current/scala/collection/immutable/List.html
