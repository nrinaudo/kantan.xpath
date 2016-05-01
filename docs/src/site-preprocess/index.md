---
layout: index
---

[![Build Status](https://travis-ci.org/nrinaudo/kantan.xpath.svg)](https://travis-ci.org/nrinaudo/kantan.xpath)
[![codecov](https://codecov.io/gh/nrinaudo/kantan.xpath/branch/master/graph/badge.svg)](https://codecov.io/gh/nrinaudo/kantan.xpath)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.nrinaudo/kantan.xpath_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.nrinaudo/kantan.xpath_2.11)
[![Join the chat at https://gitter.im/nrinaudo/kantan.xpath](https://img.shields.io/badge/gitter-join%20chat-52c435.svg)](https://gitter.im/nrinaudo/kantan.xpath)

kantan.xpath is an XPath expression evaluation library for the [Scala programming language](http://www.scala-lang.org).

## Getting started

kantan.xpath is currently available both for Scala 2.10 and 2.11.

The current version is `@VERSION@`, which can be added to your project with one or more of the following line(s)
in your SBT build file:

```scala
// Core library, included automatically if any other module is imported.
libraryDependencies += "com.nrinaudo" %% "kantan.xpath" % "@VERSION@"

// Provides scalaz type class instances.
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-scalaz" % "@VERSION@"

// Provides cats type class instances.
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-cats" % "@VERSION@"

// Provides "dirty" HTML parsing through NekoHTML.
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-nekohtml" % "@VERSION@"
```

## Motivation

Web scrapping is something that I have to do with some regularity, and the standard Scala XML library is not well suited
for that task - not as well, at least, as a proper XPath implementation.

The standard Java library, on the other hand, has an acceptably efficient XPath API, but one that is hobbled, from a
Scala perspective, by terrible types and safety issues. Everything throws and everything is a node.

kantan.xpath, then, is a thin layer over that Java API that attempts to provide much needed safety, useful types
and as little boilerplate as possible.
