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

* [`MonadError`] and [`SemigroupK`] for [`NodeDecoder`].
* [`Contravariant`] for [`XmlSource`].
* [`Show`] and [`Eq`] for all error types ([`XPathError`] and all its descendants).
* [`Show`] for [`Node`].

[`MonadError`]:https://typelevel.org/cats/api/cats/MonadError.html
[`SemigroupK`]:https://typelevel.org/cats/api/cats/SemigroupK.html
[`Contravariant`]:http://typelevel.org/cats/api/cats/Contravariant.html
[`Show`]:https://typelevel.org/cats/api/cats/Show.html
[`Eq`]:https://typelevel.org/cats/api/cats/kernel/Eq.html
[`NodeDecoder`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html
[`Node`]:{{ site.baseurl }}/api/kantan/xpath/index.html#Node=org.w3c.dom.Node
[`XPathError`]:{{ site.baseurl }}/api/kantan/xpath/XPathError.html
[`XmlSource`]:{{ site.baseurl }}/api/kantan/xpath/XmlSource.html
