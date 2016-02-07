package kantan.xpath

import laws.discipline.discipline.NodeDecoderTests
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class IntTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("NodeDecoder[Int]", NodeDecoderTests.cdataEncoded[Int](_.toString).nodeDecoder)
}
