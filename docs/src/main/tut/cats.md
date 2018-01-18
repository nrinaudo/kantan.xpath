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
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-cats" % "@VERSION@"
```

You then need to import the corresponding package:

```tut:silent
import kantan.xpath.cats._
```

## Cats instances

The following instance for cats type classes are provided:

* [`Functor`] for [`NodeDecoder`].

[`Functor`]:http://typelevel.org/cats/api/cats/Functor.html
[`NodeDecoder`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html
[`XPathResult`]:{{ site.baseurl }}/api/kantan/xpath/XPathResult$.html
[`ReadResult`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package$$ReadResult
[`ParseResult`]:{{ site.baseurl }}/api/kantan/xpath/package$$ParseResult.html
[`DecodeResult`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package$$DecodeResult
[`CompileResult`]:{{ site.baseurl }}/api/kantan/xpath/CompileResult$.html
