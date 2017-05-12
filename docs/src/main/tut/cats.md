---
layout: tutorial
title: "Cats module"
section: tutorial
sort_order: 12
---
kantan.xpath has a [cats](https://github.com/typelevel/cats) module that is, in its current incarnation, fairly bare
bones: it provides a few useful type class instances.

The `cats` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-cats" % "0.1.10"
```

You then need to import the corresponding package:

```tut:silent
import kantan.xpath.cats._
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

[`Functor`]:http://typelevel.org/cats/api/cats/Functor.html
[`BiFunctor`]:http://typelevel.org/cats/api/cats/functor/Bifunctor.html
[`Order`]:http://typelevel.org/cats/api/cats/kernel/Order.html
[`Show`]:http://typelevel.org/cats/api/cats/Show.html
[`Traverse`]:http://typelevel.org/cats/api/cats/Traverse.html
[`Monad`]:http://typelevel.org/cats/api/cats/Monad.html
[`Monoid`]:http://typelevel.org/cats/api/cats/kernel/Monoid.html
[`NodeDecoder`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html
[`XPathResult`]:{{ site.baseurl }}/api/kantan/xpath/XPathResult$.html
[`ReadResult`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package$$ReadResult
[`ParseResult`]:{{ site.baseurl }}/api/kantan/xpath/package$$ParseResult.html
[`DecodeResult`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package$$DecodeResult
[`CompileResult`]:{{ site.baseurl }}/api/kantan/xpath/CompileResult$.html
