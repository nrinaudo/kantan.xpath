---
layout: default
title:  "Scrapping Metacritic"
section: tutorial
---
One of the reasons kantan.xpath was created was because I had it in my head to run various ml algorithms on
videogame [Metacritic] data - are there, for instance, interesting correlations between a game's genre and its scores?
Or do some publications consistently rank titles from a given publisher higher than others?

As is often the case, the main difficulty was not to analyse the data but actually get my hands on it. I hoped
[Metacritic] would provide an API or, baring that, acceptably up-to-date dumps, but no such luck. I then set about
writing a scrapper, which turned out to be rather painful - not because of anything smart [Metacritic] was doing to 
protect their data, but because the [standard Scala XML library](https://github.com/scala/scala-xml) fought me every step of the way.

That's how kantan.xpath came about, and this tutorial shows how comparatively simple scrapping can be, with the proper
tools.

## Setting things up

### Default imports
The following imports are assumed in the rest of this tutorial:

```tut:silent
import kantan.xpath._          // Basic kantan.xpath types.
import kantan.xpath.ops._      // Implicit operators.
import kantan.xpath.nekohtml._ // HTML parsing.
import java.net.URI
```

Note that `kantan.xpath.nekohtml` requires the NekoHTML module, so make sure to update your build dependencies
accordingly.



### Data Structures
The first thing to do is to create the types in which our game information will be stored. To keep things simple,
let's represent:

* a game as a name and a list of reviews
* a review as a critic name and an integer score

```tut:silent
case class Review(critic: String, score: Int)

case class Game(name: String, reviews: List[Review])
```
  
More data could be scrapped (developer, genre, rating...) but what we've defined there is sufficient for demonstration
purposes.

## Parsing XML and HTML
Any type that has an instance of [`XmlSource`] is enriched with an [`asNode`] method that will attempt to parse it
as an XML document. Default instances are provided for most common types where it makes sense, such as strings:

```tut
"<root><node>text</node></root>".asNode
```

Note that the return value is actually wrapped in a [`LoadingResult`], which you can correctly think of as a specialised
version of [`Option`].

More interesting for our purposes, there is a default [`XmlSource`] instance for [`URI`], which allows us to turn any
URI into an XML document. Let's try and download a [Metacritic] page:

```tut
new URI("http://www.metacritic.com/browse/games/title/gamecube").asNode
```

This fails, because [Metacritic] apparently does not want people to scrap their data. They're however only using the
most rudimentary protection: a filter on a request's user agent, apparently a simple black list of known programmatic
clients.

Bypassing this is relatively trivial: simply setting your user-agent to something other than Java's default will do the
trick.

kantan.xpath having been made with scrapping in mind, it has specific support for this. All you need to do is declare
an appropriate instance of [`XmlSource`]:

```tut:silent
implicit val uriSource = XmlSource.uri.withUserAgent("kantan.xpath/0.1.0")
```

Things should work fine now:

```tut
new URI("http://www.metacritic.com/browse/games/title/gamecube").asNode
```

## Retrieving a list of game URIs
[Metacritic] organises its game listings by platforms, then by first letter in the name. We'll focus on a single
platform here, so our first step needs to be, for a given platform, to retrieve the URIs of each "letter" page.
  
Exploring the DOM of a platform page with, for example, a browser's debugging tools, we find that the following XPath
expression yields the URIs we're after: `//ul[@class='letternav']//a/@href`.

This can be represented as a Scala object fairly easily through the [`xpath`] method that enriches strings:

```tut
"//ul[@class='letternav']//a/@href".xpath
```

Note that this method is unsafe: it will throw an exception on ill-formed XPath expressions. Should you prefer a safe
alternative, you can always use [`Expression.apply`], which wraps the result in an [`Option`]:

```tut
Expression("//ul[@class='letternav']//a/@href")
```

Now that we have this expression, we simply need to evaluate it on our platform page. kantan.xpath provides various ways
of doing that, the simplest, most idiomatic one being the [`all`] method that enriches any type that has an
[`XmlSource`] in scope. We could then write, as a first attempt:

```tut:silent
def indexes(platform: String): XPathResult[List[URI]] =
  new URI(s"http://www.metacritic.com/browse/games/title/$platform").all[List, URI]("//ul[@class='letternav']//a/@href".xpath)
```

Note that in addition to the xpath expression, [`all`] has two type parameters:

* the type of the collection in which to store results, [`List`] in our code. This can be anything that has an instance
  of [`CanBuildFrom`], which essentially means any standard collections.
* the type each result should be represented as, [`URI`] in our example. This can be anything that has an instance of
  [`NodeDecoder`] in scope (which is most standard types).

Our implementation suffers from two issues, however:

* the returned list contains all the URIs we're after, except for platform page itself. We need to add that to the list
  before returning it.
* the returned URIs are relative, and we really want canonical ones to be able to download them.

Fixing these issues is relatively simple:

```tut:silent
def indexes(platform: String): XPathResult[List[URI]] = {
  val root = new URI(s"http://www.metacritic.com/browse/games/title/$platform")

  root.all[List, URI]("//ul[@class='letternav']//a/@href".xpath).map(root :: _.map(root.resolve))
}
```

Each index page contains a list of games, the URI of which can be extracted with the following XPath expression:
`//div[@class='basic_stat product_title']/a/@href`.

Turning an index page URI into a list of game URIs is thus as simple as:

```tut:silent
def gamesFromIndex(index: URI): XPathResult[List[URI]] =
  index.all[List, URI]("//div[@class='basic_stat product_title']/a/@href".xpath).
    map(_.map(u ⇒ index.resolve(u + "/critic-reviews")))
```

Note that there's a small subtlety here: we don't use the raw URI, but append `/critic-reviews` instead. This is just
an optimisation, it allows us to avoid having to parse each game URI to extract that of the complete list of reviews.
Since the later always follows the same pattern, it's faster and simpler to just hard-code it.


## Extracting games and reviews
We now get to the meat of the problem: given the URI of a game, turn it into an instance of the `Game` class we declared
earlier.

Turning the URI into an XML document is something we know how to do since the last section, but extracting custom types
through XPath expressions is new: we've only extracted instances of [`URI`] so far. Granted, we could extract each
component of `Game` individually and then assemble them, but that's neither terribly elegant nor idiomatic.

kantan.xpath provides a somewhat more pleasant alternative: deriving instances of [`NodeDecoder`] through the use of the
[`decoderXXX`] method, where `XXX` stands for the number of values required to build an instance of the
desired type. `Game`, for example, has two fields, so we'll need to use [`decoder2`].

This method expects a function that turns a list of values into an instance of the desired types, as well as an XPath
expression per value to extract. There's a small twist, though: the types of the values we need to extract must have
a [`NodeDecoder`] in scope. `Game` has two fields, one of which definitely does not have such instance in scope:
`List[Review]`.

We get the `List` part for free, but we need to write a `NodeDecoder[Review]`. `Review` is only composed of basic types
for which default instances of [`NodeDecoder`] exist, so we can go ahead and use [`decoder2`]:

```tut:silent
val critic = ".//div[@class='review_critic']".xpath
val score = ".//div[@class='review_grade']".xpath

implicit val reviewDecoder: NodeDecoder[Review] = NodeDecoder.decoder2(Review.apply)(critic, score)
```

Now that we have decoder for `Review`, we can write one for `Game` fairly easily:

```tut:silent
val title = "//h1[@class='product_title']/a".xpath
val reviews = "//div[contains(@class, 'critic_reviews_module')]//div[@class='review_content']".xpath

implicit val gameDecoder: NodeDecoder[Game] =
  NodeDecoder.decoder2(Game.apply)(title, reviews).map(g ⇒ g.copy(name = g.name.trim))
```

Having done that, turning a [`URI`] into a `Game` is almost the same thing as turning one into a list of [`URI`] as
we've done so far, with the small difference that, since we only want one, we use [`first`] rather than [`all`]:

```tut:silent
def game(uri: URI): XPathResult[Game] = uri.first[Game]("//body".xpath)
```

Putting everything together, we can now write a simple function that takes a platform name and returns a list of games:

```tut:silent
def gamesFor(platform: String): List[Game] =
  indexes(platform).get.flatMap(i ⇒ gamesFromIndex(i).get).map(uri ⇒ game(uri).get)
```

Note that this last function is unsafe: it will throw an exception as soon as an error occurs, rather than encode
failure in its return type. The only reason for that is that, since we're interweaving lists and decode results, the
code would require either hard-to-follow nested maps and flatMaps or the use of monad transformers, which is rather
off topic for a simple XPath tutorial.

[`URI`]:https://docs.oracle.com/javase/7/docs/api/java/net/URI.html
[Metacritic]:http://www.metacritic.com
[`XmlSource`]:{{ site.baseurl }}/api/#kantan.xpath.XmlSource
[`asNode`]:{{ site.baseurl }}/api/#kantan.xpath.XmlSource@asNode(a:A):kantan.xpath.LoadingResult
[`LoadingResult`]:{{ site.baseurl }}/api/#kantan.xpath.package@LoadingResult=kantan.codecs.Result[kantan.xpath.XPathError.LoadingError,kantan.xpath.package.Node]
[`Option`]:http://www.scala-lang.org/api/current/index.html#scala.Option
[`xpath`]:{{ site.baseurl }}/api/#kantan.xpath.ops$$RichString@xpath(implicitcomp:kantan.xpath.XPathCompiler):kantan.xpath.Expression
[`Expression.apply`]:{{ site.baseurl }}/api/#kantan.xpath.Expression$@apply(str:String)(implicitcompiler:kantan.xpath.XPathCompiler):Option[kantan.xpath.Expression]
[`all`]:{{ site.baseurl }}/api/#kantan.xpath.XmlSource@all[F[_],B](a:A,expr:kantan.xpath.Expression)(implicitevidence$2:kantan.xpath.NodeDecoder[B],implicitcbf:scala.collection.generic.CanBuildFrom[Nothing,B,F[B]]):kantan.xpath.XPathResult[F[B]]
[`first`]:{{ site.baseurl }}/api/#kantan.xpath.XmlSource@first[B](a:A,expr:kantan.xpath.Expression)(implicitevidence$1:kantan.xpath.NodeDecoder[B]):kantan.xpath.XPathResult[B]
[`List`]:http://www.scala-lang.org/api/current/index.html#scala.collection.immutable.List
[`CanBuildFrom`]:http://www.scala-lang.org/api/current/index.html#scala.collection.generic.CanBuildFrom
[`NodeDecoder`]:{{ site.baseurl }}/api/#kantan.xpath.NodeDecoder
[`decoderXXX`]:{{ site.baseurl }}/api/#kantan.xpath.NodeDecoder$@decoder2[I1,I2,O](f:(I1,I2)=>O)(x1:kantan.xpath.Expression,x2:kantan.xpath.Expression)(implicite1:kantan.xpath.Evaluator[I1],implicite2:kantan.xpath.Evaluator[I2]):kantan.xpath.NodeDecoder[O]
[`decoder2`]:{{ site.baseurl }}/api/#kantan.xpath.NodeDecoder$@decoder2[I1,I2,O](f:(I1,I2)=>O)(x1:kantan.xpath.Expression,x2:kantan.xpath.Expression)(implicite1:kantan.xpath.Evaluator[I1],implicite2:kantan.xpath.Evaluator[I2]):kantan.xpath.NodeDecoder[O]