package kantan.xpath.scalaz

import kantan.xpath.{DecodeError, ParseError, ReadError}
import kantan.xpath.laws.discipline.arbitrary._
import scalaz.scalacheck.ScalazProperties.equal

class InstancesTests extends ScalazSuite {
  checkAll("ReadError", equal.laws[ReadError])
  checkAll("DecodeError", equal.laws[DecodeError])
  checkAll("ParseError", equal.laws[ParseError])
}
