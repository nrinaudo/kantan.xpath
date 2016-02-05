package kantan.xpath

import laws.discipline.discipline.NodeDecoderTests
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class ShortTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("Short", NodeDecoderTests.cdataEncoded[Short](_.toString).nodeDecoder)
}
