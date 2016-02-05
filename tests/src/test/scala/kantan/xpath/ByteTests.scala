package kantan.xpath

import laws.discipline.discipline.NodeDecoderTests
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class ByteTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("Byte", NodeDecoderTests.cdataEncoded[Byte](_.toString).nodeDecoder)
}
