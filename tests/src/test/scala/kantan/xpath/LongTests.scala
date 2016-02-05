package kantan.xpath

import kantan.xpath.laws.discipline.discipline.NodeDecoderTests
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class LongTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("Long", NodeDecoderTests.cdataEncoded[Long](_.toString).nodeDecoder)
}
