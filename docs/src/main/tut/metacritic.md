---
layout: default
title:  "Scrapping Metacritic"
section: tutorial
---
One of the reasons kantan.xpath was created was because I had it in my head to run various ml algorithms on
videogame Metacritic data - are there, for instance, interesting correlations between a game's genre and its scores? Or
do some publications consistently rank titles from a given publisher higher than others?

As is often the case, the main difficulty was not to analyse the data but actually get my hands on it. I hoped
Metacritic would provide an API or, baring that, acceptably up-to-date dumps of their data, but no such luck. So I set
about writing a scrapper, which turned out to be a huge hassle - not because of anything smart Metacritic was doing
to protect their data, but because the standard Scala XML library fought me every step of the way.

That's how kantan.xpath came about, and this tutorial shows how comparatively simple scrapping can be, with the proper
tools.


## Setting things up

### Default imports

```tut:silent
import kantan.xpath._
import kantan.xpath.ops._
import kantan.xpath.nekohtml._
import java.net.URI
```

### Data Structures
The first thing to do is to create the types in which our game information will be stored. To keep things simple,
let's represent a game as a name and a list of reviews, and a review as a publication name and an integer score:

```tut:silent
case class Review(critic: String, score: Int)
case class Game(name: String, reviews: List[Review])
```  

### Being sneaky
Metacritic apparently does not want people to scrap their data, but they're only using the most rudimentary protection:
a filter on a request's user agent, apparently a simple black list of known programmatic clients.

Bypassing this is relatively trivial: simply setting your user-agent to something other than Java's default will do the
trick.

Kantan.xpath having been made with scrapping in mind, it has specific support for this. All you need to do is to
write an appropriate [`XmlSource`] instance for [`URI`], which can be achieved as follows:

```tut:silent
implicit val uriSource = XmlSource.uri.withUserAgent("kantan.xpath/0.1.0")
```

## Getting a list of game URIs

```tut:silent
def indexes(platform: String): List[URI] = {
  val root = new URI(s"http://www.metacritic.com/browse/games/title/$platform")

  root :: "//ul[@class='letternav']//a/@href".xpath.unsafe.all[List, URI](root.asUnsafeNode).map(root.resolve)
}
```

```tut:silent
def gamesFromIndex(index: URI): List[URI] =
  "//div[@class='basic_stat product_title']/a/@href".xpath.unsafe.all[List, URI](index.asUnsafeNode).
    map(u => index.resolve(u + "/critic-reviews"))
```

```tut:silent
val critic = ".//div[@class='review_critic']".xpath
val score = ".//div[@class='review_grade']".xpath
implicit val reviewND: NodeDecoder[Review] = NodeDecoder.decoder2(Review.apply)(critic, score)
```

```tut:silent
val title = "//h1[@class='product_title']/a".xpath
val reviews = "//div[contains(@class, 'critic_reviews_module')]//div[@class='review_content']".xpath

implicit val gameND: NodeDecoder[Game] =
  NodeDecoder.decoder2(Game.apply)(title, reviews).map(g => g.copy(name = g.name.trim))
```

```tut:silent
def game(gameUri: URI): Game = {
  val doc = gameUri.asUnsafeNode

  Game("//h1[@class='product_title']/a".xpath.unsafe.first[String](doc).trim,
    "//div[contains(@class, 'critic_reviews_module')]//div[@class='review_content']".xpath.unsafe.all[List, Review](doc))
}
```

```tut:silent
def games(platform: String): List[Game] = indexes(platform).flatMap(gamesFromIndex).map(game)

val platforms = List("ps4", "xboxone", "ps3", "xbox360", "pc", "wii-u", "3ds", "vita",
  "ps2", "ps", "xbox", "wii", "ds", "gamecube", "n64", "gba", "psp", "dreamcast")
```

```scala
val allGames: List[Game] = platforms.flatMap(indexes).flatMap(gamesFromIndex).map(game)
```

[`URI`]:https://docs.oracle.com/javase/7/docs/api/java/net/URI.html