# kantan.xpath

[![Build Status](https://travis-ci.org/nrinaudo/kantan.xpath.svg?branch=master)](https://travis-ci.org/nrinaudo/kantan.xpath)
[![codecov](https://codecov.io/gh/nrinaudo/kantan.xpath/branch/master/graph/badge.svg)](https://codecov.io/gh/nrinaudo/kantan.xpath)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.nrinaudo/kantan.xpath_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.nrinaudo/kantan.xpath_2.13)
[![Join the chat at https://gitter.im/nrinaudo/kantan.xpath](https://img.shields.io/badge/gitter-join%20chat-52c435.svg)](https://gitter.im/nrinaudo/kantan.xpath)

I find myself having to scrape websites with some regularity, and Scala always makes the whole process
more painful than it really needs to be - the standard XML API is ok, I suppose, but the lack of XPath support
(or actually usable XPath-like DSL) is frustrating.

kantan.xpath is a thin wrapper around the Java XPath API that attempts to be type safe, pleasant to use and hide the
nasty Java XML types whenever possible.

Documentation and tutorials are available on the [companion site](https://nrinaudo.github.io/kantan.xpath/), but for
those looking for a few quick examples:

```scala
import kantan.xpath._           // Basic kantan.xpath types.
import kantan.xpath.implicits._ // Implicit operators and literals.
import kantan.xpath.nekohtml._  // HTML parsing.
import java.net.URI

// Parses an URI as an XML document, finds interesting nodes, extracts their values as ints and store them in a list.
new URI("http://some.server.com").evalXPath[List[Int]](xp"//h1/span[@class='num']")

// Similar, but parsing tuples rather than ints and storing the results in a set.
implicit val decode: NodeDecoder[(String, Boolean)] = NodeDecoder.tuple[String, Boolean](xp"./@name", xp"./@count")
new URI("http://some.other.server.com").evalXPath[Set[(String, Boolean)]](xp"//name")

// Same as above, but only looks for the first match.
new URI("http://some.other.server.com").evalXPath[(String, Boolean)](xp"//name")
```

kantan.xpath is distributed under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0.html).
