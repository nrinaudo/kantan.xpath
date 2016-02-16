package kantan.xpath

import kantan.xpath.laws.discipline.NodeDecoderTests
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class BigIntTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("NodeDecoder[BigInt]", NodeDecoderTests.cdataEncoded[BigInt](_.toString).nodeDecoder)
}
