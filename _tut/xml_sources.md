---
layout: tutorial
title: "What can be parsed as XML data?"
section: tutorial
sort_order: 7
---
Before we can even think about applying XPath expressions to an XML document, we need to get our hands on that document
somehow. kantan.xpath extends most things that "can be turned into XML" with useful methods, such as the oft-used
[`evalXPath`] method. Among such things are:

* [`URL`]
* [`URI`]
* [`File`]
* [`String`]

This is done through the [`XmlSource`] type class: any type `A` such that there exists a value of type
[`XmlSource[A]`][`XmlSource`] in the implicit scope will be enriched with
[useful methods]({{ site.baseurl }}/api/#kantan.xpath.ops$$XmlSourceOps).

Implementing our own [`XmlSource`] for types that aren't supported by default is fairly simple.

## Implementation from scratch

Reduced to its simplest expression, an [`XmlSource[A]`][`XmlSource`] is essentially an `A ⇒ ParseResult[Node]` - that
is, a function that takes an `A` and turns it into a [`Node`], with the possibility of safe failure encoded in
[`ParseResult`].

If you can write such a function, you can trivially turn it into a valid instance of [`XmlSource`]. A simple example
would be to provide an [`XmlSource`] instance for [`Node`]:

```scala
import kantan.xpath._

implicit val node: XmlSource[Node] = XmlSource.from(ParseResult.success)
```

## Adapting existing instances

A more idiomatic way of writing new [`XmlSource`] instances, however, is to adapt existing ones through [`contramap`]
or [`contramapResult`].
The most useful instance for such purpose is the one that exists for [`InputSource`], provided an implicit instance of
[`XmlParser`] is in scope.

[`XmlParser`] is a concept most people will never really need to concern themselves about - a reasonable default
implementation is always in scope. The only reason to write a new one would be to change XML parsing behaviour, as
the [`nekohtml`] module does.

In order to write an [`XmlSource`] instance for [`String`], say, one would only need to know how to turn an instance
of [`String`] into one of [`InputSource`]:

```scala
implicit def stringSource(implicit parser: XmlParser): XmlSource[String] =
  XmlSource[InputSource].contramap(s ⇒ new InputSource(new java.io.StringReader(s)))
```


[`URL`]:https://docs.oracle.com/javase/7/docs/api/java/net/URL.html
[`URI`]:https://docs.oracle.com/javase/7/docs/api/java/net/URI.html
[`File`]:https://docs.oracle.com/javase/7/docs/api/java/io/File.html
[`String`]:https://docs.oracle.com/javase/7/docs/api/java/lang/String.html
[`XmlSource`]:{{ site.baseurl }}/api/#kantan.xpath.XmlSource
[`Node`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package@Node=org.w3c.dom.Node
[`ParseResult`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package@ParseResult=kantan.codecs.Result[kantan.xpath.ParseError,kantan.xpath.package.Node]
[`InputSource`]:{{ site.baseurl }}/api/index.html#kantan.xpath.package@InputSource=org.xml.sax.InputSource
[`XmlParser`]:{{ site.baseurl }}/api/index.html#kantan.xpath.XmlParser
[`contramap`]:{{ site.baseurl }}/api/index.html#kantan.xpath.XmlSource@contramap[B](f:B=>A):kantan.xpath.XmlSource[B]
[`nekohtml`]:{{ site.baseurl }}/api/#kantan.xpath.nekohtml.package
[`evalXPath`]:{{ site.baseurl }}/api/index.html#kantan.xpath.ops$$XmlSourceOps@evalXPath[B](expr:String)(implicitevidence$2:kantan.xpath.Compiler[B],implicitsource:kantan.xpath.XmlSource[A]):kantan.xpath.XPathResult[B]
[`contramapResult`]:{{ site.baseurl }}/api/index.html#kantan.xpath.XmlSource@contramapResult[B](f:B=>kantan.xpath.ParseResult[A]):kantan.xpath.XmlSource[B]
