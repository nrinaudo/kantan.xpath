---
layout: tutorial
title: "Decoding nodes as primitive types"
section: tutorial
sort: 1
---
The simplest possible use of kantan.xpath is to extract primitive types from XML documents.

In order to show how that works, we'll first need some sample XML data, which we'll get from this project's resources:

```tut:silent
val rawData: java.net.URL = getClass.getResource("/simple.xml")
```

This is what we're working with:

```tut
scala.io.Source.fromURL(rawData).mkString
```

We'll then need to import kantan.xpath's syntax, which will let us evaluate XPath expressions directly on something
that can be turned into an XML document:

```tut:silent
import kantan.xpath.ops._
```

This allows us to write the following code, which will attempt to extract the `id` field of any `element` node as an
`Int`:

```tut
rawData.evalXPath[Int]("//element/@id")
```


There are a few things worth pointing out here. First, the return type: you might expect an `Int`, since this is what
you requested from [`evalXPath`], but we got an [`XPathResult[Int]`][`XPathResult`] instead. An [`XPathResult`] is 
either a failure if something went wrong (the XPath expression is not valid, the `id` field is not a valid `Int`....) or
a success otherwise. This mechanism ensures that [`evalXPath`] is safe: no exception will be thrown and break the flow 
of your code.

In some cases, however, we don't really care for runtime safety and are fine with our program crashing at the first
error. This is what the [`unsafeEvalXPath`] method was designed for:  

```tut
rawData.unsafeEvalXPath[Int]("//element/@id")
```


Another point of interest is that the sample XML file contained multiple `element` nodes, but we only got the `id`
attribute of the first one. This is due to the type parameter we passed to [`evalXPath`]: by requesting a non-collection
type, we told kantan.xpath that we only wanted the first result. We could get them all by requesting a 
[`List[Int]`][`List`], for example:


```tut
rawData.evalXPath[List[Int]]("//element/@id")
```

Any type constructor that has a [`CanBuildFrom`] instance could have been used instead of [`List`] - that's essentially
all collections. By the same token, any primitive time could have been used instead of `Int`. For example:

```tut
rawData.evalXPath[Vector[Boolean]]("//element/@enabled")
```

[`evalXPath`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.ops$$XmlSourceOps@evalXPath[B](expr:String)(implicitevidence$2:kantan.xpath.Compiler[B],implicitsource:kantan.xpath.XmlSource[A]):kantan.xpath.XPathResult[B]
[`unsafeEvalXPath`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.ops$$XmlSourceOps@unsafeEvalXPath[B](expr:String)(implicitevidence$1:kantan.xpath.Compiler[B],implicitsource:kantan.xpath.XmlSource[A]):B
[`XPathResult`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.package@XPathResult[A]=kantan.codecs.Result[kantan.xpath.XPathError,A]
[`CanBuildFrom`]:http://www.scala-lang.org/api/2.11.8/#scala.collection.generic.CanBuildFrom
[`List`]:http://www.scala-lang.org/api/2.11.8/#scala.collection.immutable.List