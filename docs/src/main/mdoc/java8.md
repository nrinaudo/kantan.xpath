---
layout: scala mdocorial
title: "Java 8 dates and times"
section: scala mdocorial
sort_order: 11
---
Java 8 comes with a better thought out dates and times API. Unfortunately, it cannot be supported as part of the core
kantan.xpath API - we still support Java 7. There is, however, a dedicated optional module that you can include by
adding the following line to your `build.sbt` file:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-java8" % "@VERSION@"
```

You then need to import the corresponding package:

```scala mdoc:silent
import kantan.xpath.java8._
```

And this will bring [`NodeDecoder`] instances in scope for the following types:

* [`Instant`]
* [`LocalDateTime`]
* [`ZonedDateTime`]
* [`OffsetDateTime`]
* [`LocalDate`]
* [`LocalTime`]

These will use the default Java 8 formats. For example:

```scala mdoc:silent
import kantan.xpath._
import kantan.xpath.implicits._
import java.time._

val input = "<root><date value='1978-10-12'/><date value='2015-01-09'/></root>"
```

We can decode the bracketed dates without providing an explicit decoder:

```scala mdoc
input.evalXPath[List[LocalDate]](xp"//date/@value")
```

It's also possible to provide your own format. For example, for [`LocalDateTime`]:

```scala mdoc:reset:silent
import java.time._
import java.time.format.DateTimeFormatter
import kantan.xpath._
import kantan.xpath.implicits._

import kantan.xpath.java8._

val input = "<root><date value='12-10-1978'/><date value='09-01-2015'/></root>"

implicit val decoder: NodeDecoder[LocalDate] = localDateDecoder(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
```

And we can now simply write:

```scala mdoc
input.evalXPath[List[LocalDate]](xp"//date/@value")
```

Note that while you can pass a [`DateTimeFormatter`] directly, the preferred way of dealing with pattern strings is to
use the literal syntax provided by kantan.xpath:

```scala mdoc:silent
localDateDecoder(fmt"dd-MM-yyyy")
```

The advantage is that this is checked at compile time - invalid pattern strings will cause a compilation error:

```scala mdoc:fail
localDateDecoder(fmt"FOOBAR")
```

[`NodeDecoder`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html
[`Instant`]:https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html
[`LocalDateTime`]:https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html
[`OffsetDateTime`]:https://docs.oracle.com/javase/8/docs/api/java/time/OffsetDateTime.html
[`ZonedDateTime`]:https://docs.oracle.com/javase/8/docs/api/java/time/ZonedDateTime.html
[`LocalDate`]:https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
[`LocalTime`]:https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html
[`DateTimeFormatter`]:https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
