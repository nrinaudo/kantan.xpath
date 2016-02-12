package kantan.xpath

import kantan.xpath.laws.discipline.NodeDecoderTests
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class ShortTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("NodeDecoder[Short]", NodeDecoderTests.cdataEncoded[Short](_.toString).nodeDecoder)
}
