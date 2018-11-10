---
layout: tutorial
title: "Joda time module"
section: tutorial
sort_order: 10
---
[Joda-Time](http://www.joda.org/joda-time/) is a very well thought out date and time library for Java that happens to
be very popular in Scala - at the very least, it's quite a bit better than the stdlib [`Date`]. kantan.xpath provides
support for it through a dedicated module.

The `joda-time` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-joda-time" % "0.5.0"
```

You then need to import the corresponding package:

```scala
import kantan.xpath.joda.time._
```

kantan.xpath has default, ISO 8601 compliant [`NodeDecoder`] instances for the following types:

* [`DateTime`]
* [`LocalDate`]
* [`LocalDateTime`]
* [`LocalTime`]

Let's imagine for example that we want to extract dates from the following string:

```scala
import kantan.xpath._
import kantan.xpath.implicits._

val input = "<root><date value='1978-10-12'/><date value='2015-01-09'/></root>"
```

This is directly supported:

```scala
scala> input.evalXPath[List[org.joda.time.LocalDate]](xp"//date/@value")
res0: kantan.xpath.XPathResult[List[org.joda.time.LocalDate]] = Right(List(1978-10-12, 2015-01-09))
```

It is, of course, possible to declare your own [`NodeDecoder`]. This is, for example, how you'd create a custom
[`NodeDecoder[LocalDate]`][`NodeDecoder`]:

```scala
import org.joda.time.LocalDate
import org.joda.time.format._

val input = "<root><date value='12-10-1978'/><date value='09-01-2015'/></root>"

implicit val decoder: NodeDecoder[LocalDate] = localDateDecoder(DateTimeFormat.forPattern("dd-MM-yyyy"))
```

And we're done, as far as decoding is concerned. We only need to get an XPath expression together and evaluate it:

```scala
scala> input.evalXPath[List[org.joda.time.LocalDate]](xp"//date/@value")
res1: kantan.xpath.XPathResult[List[org.joda.time.LocalDate]] = Right(List(1978-10-12, 2015-01-09))
```

Note that while you can pass a [`DateTimeFormatter`] directly, the preferred way of dealing with pattern strings is to
use the literal syntax provided by kantan.xpath:

```scala
localDateDecoder(fmt"dd-MM-yyyy")
```

The advantage is that this is checked at compile time - invalid pattern strings will cause a compilation error:

```scala
scala> localDateDecoder(fmt"FOOBAR")
<console>:27: error: Invalid pattern: 'FOOBAR'
       localDateDecoder(fmt"FOOBAR")
                            ^
```

[`Date`]:https://docs.oracle.com/javase/7/docs/api/java/util/Date.html
[`DateTime`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/DateTime.html
[`LocalDate`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/LocalDate.html
[`LocalDateTime`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/LocalDateTime.html
[`LocalTime`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/LocalTime.html
[`DateTimeFormat`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/format/DateTimeFormat.html
[`NodeDecoder`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html
[`DateTimeFormatter`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/format/DateTimeFormatter.html
