---
layout: tutorial
title: "Scalaz module"
section: tutorial
sort: 12
---
kantan.xpath has a [scalaz](https://github.com/scalaz/scalaz) module that is, in its current incarnation, fairly bare
bones: it provides decoders for [`Maybe`] and [`\/`] as well as a few useful type class instances.
 
The `scalaz` module can be used by adding the following dependency to your `build.sbt`:
 
```scala
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-scalaz" % "0.1.4"
```
 
You then need to import the corresponding package:
 
```tut:silent
import kantan.xpath.scalaz._
```

## `\/` decoder

The `scalaz` module provides a [`NodeDecoder`] instance for [`\/`]: for any type `A` and `B` that each have a
[`NodeDecoder`] instance, there exists a [`NodeDecoder`] instance for `A \/ B`.

First, a few imports:

```tut:silent
import scalaz._
import kantan.xpath.implicits._
```

We can then simply write the following:

```tut
"<foo><bar value='1'/><bar value='foo'/></foo>".evalXPath[List[Int \/ String]](xp"//bar/@value")
```

## `Maybe` decoder

The `scalaz` module provides a [`NodeDecoder`] instance for [`Maybe`]: for any type `A` that has a [`NodeDecoder`]
instance, there exists a [`NodeDecoder`] instance for `Maybe[A]`.

```tut
"<foo><bar/></foo>".evalXPath[Maybe[Int]](xp"//bar/@value")
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
[`NodeDecoder`]:{{ site.baseUri }}/api/index.html#kantan.xpath.package@NodeDecoder[A]=kantan.codecs.Decoder[Option[kantan.xpath.package.Node],A,kantan.xpath.DecodeError,kantan.xpath.codecs.type]
[`XPathResult`]:{{ site.baseUri }}/api/index.html#kantan.xpath.package@XPathResult[A]=kantan.codecs.Result[kantan.xpath.XPathError,A]
[`ReadResult`]:{{ site.baseUri }}/api/index.html#kantan.xpath.package@ReadResult[A]=kantan.codecs.Result[kantan.xpath.ReadError,A]
[`ParseResult`]:{{ site.baseUri }}/api/index.html#kantan.xpath.package@ParseResult[A]=kantan.codecs.Result[kantan.xpath.ParseError,A]
[`DecodeResult`]:{{ site.baseUri }}/api/index.html#kantan.xpath.package@DecodeResult[A]=kantan.codecs.Result[kantan.xpath.DecodeError,A]
[`CompileResult`]:{{ site.baseUri }}/api/index.html#kantan.xpath.package@CompileResult[A]=kantan.codecs.Result[kantan.xpath.CompileError,A]