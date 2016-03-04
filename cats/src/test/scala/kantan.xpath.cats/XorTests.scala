package kantan.xpath.cats

import kantan.codecs.laws.{IllegalString, LegalString}
import arbitrary._
import cats.data.Xor
import kantan.xpath.Node
import kantan.xpath.laws.discipline.NodeDecoderTests
import org.scalacheck.Arbitrary
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class XorTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val legalXor = arbLegalNode[Int Xor Boolean]
  implicit val illegalXor = arbIllegalNode[Int Xor Boolean]

  checkAll("NodeDecoder[Int Xor Boolean]", NodeDecoderTests[Int Xor Boolean].decoder[Int, Int])
}
