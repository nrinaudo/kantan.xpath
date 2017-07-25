---
layout: tutorial
title: "Scalaz module"
section: tutorial
sort_order: 13
---
kantan.xpath has a [scalaz](https://github.com/scalaz/scalaz) module that is, in its current incarnation, fairly bare
bones: it provides decoders for [`Maybe`] and [`\/`] as well as a few useful type class instances.

The `scalaz` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-scalaz" % "0.2.0"
```

You then need to import the corresponding package:

```scala
import kantan.xpath.scalaz._
```

## `\/` decoder

The `scalaz` module provides a [`NodeDecoder`] instance for [`\/`]: for any type `A` and `B` that each have a
[`NodeDecoder`] instance, there exists a [`NodeDecoder`] instance for `A \/ B`.

First, a few imports:

```scala
import scalaz._
import kantan.xpath.implicits._
```

We can then simply write the following:

```scala
scala> "<foo><bar value='1'/><bar value='foo'/></foo>".evalXPath[List[Int \/ String]](xp"//bar/@value")
res0: kantan.xpath.XPathResult[List[Int \/ String]] = Success(List(-\/(1), \/-(foo)))
```

## `Maybe` decoder

The `scalaz` module provides a [`NodeDecoder`] instance for [`Maybe`]: for any type `A` that has a [`NodeDecoder`]
instance, there exists a [`NodeDecoder`] instance for `Maybe[A]`.

```scala
scala> "<foo><bar/></foo>".evalXPath[Maybe[Int]](xp"//bar/@value")
res1: kantan.xpath.XPathResult[scalaz.Maybe[Int]] = Success(Empty())
```

## Scalaz instances

The following instance for cats type classes are provided:

* [`Functor`] for [`NodeDecoder`].
* [`Order`] for all result types ([`DecodeResult`], [`XPathResult`], [`ReadResult`], [`ParseResult`] and [`CompileResult`]).
* [`Monoid`] for all result types.
* [`Show`] for all result types.
* [`Traverse`] for all result types.
* [`Monad`] for all result types.
* [`BiFunctor`] for all result types.

[`Functor`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Functor
[`BiFunctor`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Bifunctor
[`Order`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Order
[`Show`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Show
[`Traverse`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Show
[`Monad`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Monad
[`Monoid`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Monoid
[`\/`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.$bslash$div
[`Maybe`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Maybe
[`NodeDecoder`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html
[`XPathResult`]:{{ site.baseurl }}/api/kantan/xpath/XPathResult$.html
[`ReadResult`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package$$ReadResult
[`ParseResult`]:{{ site.baseurl }}/api/kantan/xpath/package$$ParseResult.html
[`DecodeResult`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package$$DecodeResult
[`CompileResult`]:{{ site.baseurl }}/api/kantan/xpath/CompileResult$.html
