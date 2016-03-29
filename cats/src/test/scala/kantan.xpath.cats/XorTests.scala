package kantan.xpath.cats

import _root_.cats.data.Xor
import kantan.xpath.cats.arbitrary._
import kantan.xpath.laws.discipline.NodeDecoderTests
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class XorTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val legalXor = arbLegalNode[Int Xor Boolean]
  implicit val illegalXor = arbIllegalNode[Int Xor Boolean]

  checkAll("NodeDecoder[Int Xor Boolean]", NodeDecoderTests[Int Xor Boolean].decoder[Int, Int])
}
