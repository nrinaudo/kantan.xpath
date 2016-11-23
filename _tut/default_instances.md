---
layout: tutorial
title: "Default instances"
section: tutorial
sort_order: 8
---

## `NodeDecoder`

### Basic types

The following types have [`NodeDecoder`] instances available out of the box:

* [`BigDecimal`]
* [`BigInt`]
* [`Byte`]
* [`Char`]
* [`Boolean`]
* [`Double`]
* [`File`]
* [`Float`]
* [`Int`]
* [`Long`]
* [`Path`]
* [`Short`]
* [`String`]
* [`UUID`]
* [`URI`]
* [`URL`]

### `java.util.Date`

There also is a default [`NodeDecoder`] instance available for [`Date`], but this one is slightly more complicated.
There are so many different ways of writing dates that there is no reasonable default behaviour - one might argue that
defaulting to ISO 8601 might make sense, but there doesn't appear to be a sane way of implementing that in Java’s crusty
date / time API.

Instead of providing a default implementation that is likely going to be incorrect for most people, kantan.xpath
provides easy tools for creating decoders from an instance of [`DateFormat`].

We could for example declare a decoder for something ISO 8601-like:

```scala
import kantan.xpath.implicits._
import kantan.xpath.NodeDecoder
import java.util.{Locale, Date}

implicit val decoder = NodeDecoder.dateDecoder(new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH))
```

And we're now capable of decoding XML content as dates:

```scala
scala> "<date>2000-01-00T00:00:00.000</date>".evalXPath[Date](xp"/date")
res1: kantan.xpath.XPathResult[java.util.Date] = Success(Fri Dec 31 00:00:00 CET 1999)
```

Note that kantan.xpath has a joda-time module, a very well thought out alternative to `java.util.Date`.

### `Either`

For any two types `A` and `B` that each have a [`NodeDecoder`], there exists a
[`NodeDecoder[Either[A, B]]`][`NodeDecoder`].


This is useful for dodgy XML data where the type of a value is not well defined - it might sometimes be an int,
sometimes a boolean, for example:

```scala
scala> "<root><either>123</either><either>true</either></root>".evalXPath[List[Either[Int, Boolean]]](xp"//either")
res2: kantan.xpath.XPathResult[List[Either[Int,Boolean]]] = Success(List(Left(123), Right(true)))
```

### `Option`

For any type `A` that has a [`NodeDecoder`], there exists a [`NodeDecoder[Option[A]]`][`NodeDecoder`].


This is useful for XML where some nodes or attributes are optional. For example:

```scala
scala> "<root><opt/></root>".evalXPath[Option[Int]](xp"//opt/@value")
res3: kantan.xpath.XPathResult[Option[Int]] = Success(None)
```

## `XmlSource`

The following types have an instance of [`XmlSource`] out of the box:

* [`Reader`]
* [`InputStream`]
* [`File`]
* `Array[Byte]`
* `Array[Char]`
* [`Path`]
* [`String`]
* [`URL`]
* [`URI`]


[`BigInt`]:http://www.scala-lang.org/api/current/scala/math/BigInt.html
[`BigDecimal`]:http://www.scala-lang.org/api/current/scala/math/BigDecimal.html
[`Byte`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Byte.html
[`Char`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Character.html
[`Boolean`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Boolean.html
[`Double`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Double.html
[`Float`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Float.html
[`Int`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html
[`Long`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Long.html
[`Short`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Short.html
[`String`]:https://docs.oracle.com/javase/7/docs/api/java/lang/String.html
[`UUID`]:https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html
[`URL`]:https://docs.oracle.com/javase/7/docs/api/java/net/URL.html
[`URI`]:https://docs.oracle.com/javase/7/docs/api/java/net/URI.html
[`Reader`]:https://docs.oracle.com/javase/7/docs/api/java/io/Reader.html
[`InputStream`]:https://docs.oracle.com/javase/7/docs/api/java/io/InputStream.html
[`File`]:https://docs.oracle.com/javase/7/docs/api/java/io/File.html
[`Path`]:https://docs.oracle.com/javase/7/docs/api/java/nio/file/Path.html
[`XmlSource`]:{{ site.baseurl }}/api/kantan/xpath/XmlSource.html
[`NodeDecoder`]:{{ site.baseurl }}/api/kantan/xpath/NodeDecoder$.html
[`Date`]:https://docs.oracle.com/javase/7/docs/api/java/util/Date.html
[`DateFormat`]:https://docs.oracle.com/javase/7/docs/api/java/text/DateFormat.html
