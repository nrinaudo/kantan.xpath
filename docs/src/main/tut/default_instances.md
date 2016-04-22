---
layout: tutorial
title: "Default instances"
section: tutorial
sort: 8
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
* [`Float`]
* [`Int`]
* [`Long`]
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

Instead of providing a default implementation that is likely going to be incorrect for most people, kantan.xpath expects 
an implicit [`DateFormat`] instance in scope, and will decode and encode using that format.

### `Either`

For any two types `A` and `B` that each have a [`NodeDecoder`], there exists a
[`NodeDecoder[Either[A, B]]`][`NodeDecoder`].


This is useful for dodgy XML data where the type of a value is not well defined - it might sometimes be an int, 
sometimes a boolean, for example.


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


[`BigInt`]:http://www.scala-lang.org/api/current/index.html#scala.math.BigInt
[`BigDecimal`]:http://www.scala-lang.org/api/current/index.html#scala.math.BigDecimal
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
[`XmlSource`]:{{ site.baseUrl }}/api/#kantan.xpath.XmlSource
[`NodeDecoder`]:{{ site.baseUrl }}/api/index.html#kantan.xpath.package@NodeDecoder[A]=kantan.codecs.Decoder[kantan.xpath.package.Node,A,kantan.xpath.DecodeError,kantan.xpath.codecs.type]
[`Date`]:https://docs.oracle.com/javase/7/docs/api/java/util/Date.html
[`DateFormat`]:https://docs.oracle.com/javase/7/docs/api/java/text/DateFormat.html