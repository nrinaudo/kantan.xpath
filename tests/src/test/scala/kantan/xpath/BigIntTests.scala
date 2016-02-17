package kantan.xpath

import kantan.xpath.laws.discipline.NodeDecoderTests
import kantan.xpath.laws.discipline.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class BigIntTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("NodeDecoder[BigInt]", NodeDecoderTests[BigInt].decoder[Int, Int])
}
