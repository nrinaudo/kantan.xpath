package kantan.xpath

import kantan.xpath.laws.discipline.discipline.NodeDecoderTests
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class BooleanTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("Boolean", NodeDecoderTests.cdataEncoded[Boolean](_.toString).nodeDecoder)
}
