package grind

import grind.laws.discipline.NodeDecoderTests
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class DoubleTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("Double", NodeDecoderTests.cdataEncoded[Double](_.toString).nodeDecoder)
}
