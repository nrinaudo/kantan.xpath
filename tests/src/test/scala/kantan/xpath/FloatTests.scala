package kantan.xpath

import laws.discipline.discipline.NodeDecoderTests
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class FloatTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("NodeDecoder[Float]", NodeDecoderTests.cdataEncoded[Float](_.toString).nodeDecoder)
}