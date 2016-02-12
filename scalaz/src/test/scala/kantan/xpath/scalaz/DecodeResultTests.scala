package kantan.xpath.scalaz

import kantan.xpath.laws.discipline.arbitrary
import arbitrary._
import kantan.xpath.DecodeResult

import scalaz.scalacheck.ScalazProperties.{equal, monad}
import scalaz.std.anyVal._

class DecodeResultTests extends ScalazSuite {
  checkAll("DecodeResult", monad.laws[DecodeResult])
  checkAll("DecodeResult[Int]", equal.laws[DecodeResult[Int]])
}
