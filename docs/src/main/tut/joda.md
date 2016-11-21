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
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-joda-time" % "0.1.7"
```

You then need to import the corresponding package:

```tut:silent
import kantan.xpath.joda.time._
```

kantan.xpath has default, ISO 8601 compliant [`NodeDecoder`] instances for the following types:

* [`DateTime`]
* [`LocalDate`]
* [`LocalDateTime`]
* [`LocalTime`]

Let's imagine for example that we want to extract dates from the following string:

```tut:silent
import kantan.xpath.implicits._

val input = "<root><date value='1978-10-12'/><date value='2015-01-09'/></root>"
```

This is directly supported:

```tut
input.evalXPath[List[org.joda.time.LocalDate]](xp"//date/@value")
```

It is, of course, possible to declare your own [`NodeDecoder`]. This is, for example, how you'd create a custom
[`NodeDecoder[LocalDate]`][`NodeDecoder`]:

```tut:silent
import org.joda.time.format._

val input = "<root><date value='12-10-1978'/><date value='09-01-2015'/></root>"

implicit val decoder = localDateDecoder(DateTimeFormat.forPattern("dd-MM-yyyy"))
```

And we're done, as far as decoding is concerned. We only need to get an XPath expression together and evaluate it:

```tut
input.evalXPath[List[org.joda.time.LocalDate]](xp"//date/@value")
```



[`Date`]:https://docs.oracle.com/javase/7/docs/api/java/util/Date.html
[`DateTime`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/DateTime.html
[`LocalDate`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/LocalDate.html
[`LocalDateTime`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/LocalDateTime.html
[`LocalTime`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/LocalTime.html
[`DateTimeFormat`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/format/DateTimeFormat.html
[`NodeDecoder`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html
