---
layout: scala mdocorial
title: "Scalaz module"
section: scala mdocorial
sort_order: 13
---
kantan.xpath has a [scalaz](https://github.com/scalaz/scalaz) module that is, in its current incarnation, fairly bare
bones: it provides decoders for [`Maybe`] and [`\/`] as well as a few useful type class instances.

The `scalaz` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-scalaz" % "@VERSION@"
```

You then need to import the corresponding package:

```scala mdoc:silent
import kantan.xpath.scalaz._
```

## `\/` decoder

The `scalaz` module provides a [`NodeDecoder`] instance for [`\/`]: for any type `A` and `B` that each have a
[`NodeDecoder`] instance, there exists a [`NodeDecoder`] instance for `A \/ B`.

First, a few imports:

```scala mdoc:silent
import scalaz._
import kantan.xpath.implicits._
```

We can then simply write the following:

```scala mdoc
"<foo><bar value='1'/><bar value='foo'/></foo>".evalXPath[List[Int \/ String]](xp"//bar/@value")
```

## `Maybe` decoder

The `scalaz` module provides a [`NodeDecoder`] instance for [`Maybe`]: for any type `A` that has a [`NodeDecoder`]
instance, there exists a [`NodeDecoder`] instance for `Maybe[A]`.

```scala mdoc
"<foo><bar/></foo>".evalXPath[Maybe[Int]](xp"//bar/@value")
```

## Scalaz instances

The following instance for cats type classes are provided:

* [`MonadError`] and [`Plus`] for [`NodeDecoder`].
* [`Contravariant`] for [`XmlSource`].
* [`Show`] and [`Equal`] for all error types ([`XPathError`] and all its descendants).
* [`Equal`] for [`Node`].

[`MonadError`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.18/scalaz/MonadError.html
[`Contravariant`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.18/scalaz/Contravariant.html
[`Functor`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.18/scalaz/Functor.html
[`Plus`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.18/scalaz/Plus.html
[`Show`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.18/scalaz/Show.html
[`Equal`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.18/scalaz/Equal.html
[`\/`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.19/scalaz/$bslash$div.html
[`Maybe`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.19/scalaz/Maybe.html
[`NodeDecoder`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html
[`XPathError`]:{{ site.baseurl }}/api/kantan/xpath/XPathError.html
[`XmlSource`]:{{ site.baseurl }}/api/kantan/xpath/XmlSource.html
[`Node`]:{{ site.baseurl }}/api/kantan/xpath/index.html#Node=org.w3c.dom.Node
