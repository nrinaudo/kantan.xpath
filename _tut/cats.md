---
layout: tutorial
title: "Cats module"
section: tutorial
sort_order: 11
---
kantan.xpath has a [cats](https://github.com/typelevel/cats) module that is, in its current incarnation, fairly bare
bones: it provides decoders for [`Xor`] as well as a few useful type class instances.

The `cats` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-cats" % "0.1.5"
```

You then need to import the corresponding package:

```scala
import kantan.xpath.cats._
```


## `Xor` decoder

The `cats` module provides a [`NodeCoder`] instance for [`Xor`]: for any type `A` and `B` that each have a
[`NodeDecoder`] instance, there exists a [`NodeCoder`] instance for `A Xor B`.

First, a few imports:

```scala
import cats.data.Xor
import kantan.xpath.implicits._
```

We can then simply write the following:

```scala
scala> "<foo><bar value='1'/><bar value='foo'/></foo>".evalXPath[List[Int Xor String]](xp"//bar/@value")
res0: kantan.xpath.XPathResult[List[cats.data.Xor[Int,String]]] = Success(List(Left(1), Right(foo)))
```

## Cats instances

The following instance for cats type classes are provided:

* [`Functor`] for [`NodeDecoder`].
* [`Order`] for all result types ([`DecodeResult`], [`XPathResult`], [`ReadResult`], [`ParseResult`] and [`CompileResult`]).
* [`Show`] for all result types.
* [`Monoid`] for all result types.
* [`Traverse`] for all result types.
* [`Monad`] for all result types.
* [`BiFunctor`] for all result types.

[`Functor`]:http://typelevel.org/cats/api/#cats.Functor
[`BiFunctor`]:http://typelevel.org/cats/api/#cats.functor.Bifunctor
[`Order`]:http://typelevel.org/cats/api/index.html#cats.package@Order[A]=cats.kernel.Order[A]
[`Show`]:http://typelevel.org/cats/api/index.html#cats.Show
[`Traverse`]:http://typelevel.org/cats/api/index.html#cats.Traverse
[`Monad`]:http://typelevel.org/cats/api/index.html#cats.Monad
[`Xor`]:http://typelevel.org/cats/api/#cats.data.Xor
[`Monoid`]:http://typelevel.org/cats/api/index.html#cats.package@Monoid[A]=cats.kernel.Monoid[A]
[`NodeDecoder`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package@NodeDecoder[A]=kantan.codecs.Decoder[Option[kantan.xpath.package.Node],A,kantan.xpath.DecodeError,kantan.xpath.codecs.type]
[`XPathResult`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package@XPathResult[A]=kantan.codecs.Result[kantan.xpath.XPathError,A]
[`ReadResult`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package@ReadResult[A]=kantan.codecs.Result[kantan.xpath.ReadError,A]
[`ParseResult`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package@ParseResult[A]=kantan.codecs.Result[kantan.xpath.ParseError,A]
[`DecodeResult`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package@DecodeResult[A]=kantan.codecs.Result[kantan.xpath.DecodeError,A]
[`CompileResult`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package@CompileResult[A]=kantan.codecs.Result[kantan.xpath.CompileError,A]
