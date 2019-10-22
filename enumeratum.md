---
layout: scala mdocorial
title: "Enumeratum module"
section: scala mdocorial
sort_order: 15
---

kantan.xpath comes with an [enumeratum](https://github.com/lloydmeta/enumeratum) module that can be used
by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.xpath-enumeratum" % "0.5.1"
```

## Name-based enumerations

When working with enumerations of type `Enum`, you should import the following package:

```scala
import kantan.xpath.enumeratum._
```

And that's pretty much it. You can now encode and decode your enumeration directly.

Let's first set our types up:

```scala
import enumeratum._

// We need to put this all in a faked out package object due to the way
// documentation is built.
object somePackage {
  sealed trait DummyEnum extends EnumEntry

  object DummyEnum extends Enum[DummyEnum] {

    val values = findValues

    case object Hello   extends DummyEnum
    case object GoodBye extends DummyEnum
    case object Hi      extends DummyEnum

  }
}
```

And a few further imports, to bring our enumeration and the kantan.csv syntax in scope:

```scala
import kantan.xpath.implicits._
import somePackage._
```


We can then simply write the following:

```scala
"<foo><bar value='Hello'/></foo>".evalXPath[DummyEnum](xp"//bar/@value")
// res0: kantan.xpath.package.XPathResult[DummyEnum] = Right(Hello)

"<foo><bar value='GoodDay'/></foo>".evalXPath[DummyEnum](xp"//bar/@value")
// res1: kantan.xpath.package.XPathResult[DummyEnum] = Left(
//   TypeError("'GoodDay' is not a member of enumeration [Hello, GoodBye, Hi]")
// )
```


## Value-based enumerations

For enumerations of type `ValueEnum`, you should import the following package:

```scala
import kantan.xpath.enumeratum.values._
```

And that's pretty much it. You can now encode and decode your enumeration directly.

Let's first set our types up:

```scala
import enumeratum.values._

// We need to put this all in a faked out package object due to the way
// documentation is built.
object somePackage {

  sealed abstract class Greeting(val value: Int) extends IntEnumEntry

  object Greeting extends IntEnum[Greeting] {

    val values = findValues

    case object Hello   extends Greeting(1)
    case object GoodBye extends Greeting(2)
    case object Hi      extends Greeting(3)
    case object Bye     extends Greeting(4)

  }

}
```

And a few further imports, to bring our enumeration and the kantan.csv syntax in scope:

```scala
import kantan.xpath.implicits._
import somePackage._
```

We can then simply write the following:

```scala
"<foo><bar value='1'/></foo>".evalXPath[Greeting](xp"//bar/@value")
// res3: kantan.xpath.package.XPathResult[Greeting] = Right(Hello)

"<foo><bar value='-1'/></foo>".evalXPath[Greeting](xp"//bar/@value")
// res4: kantan.xpath.package.XPathResult[Greeting] = Left(
//   TypeError("'-1' is not in values [1, 2, 3, 4]")
// )
```

