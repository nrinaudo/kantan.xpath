package grind

import grind.laws.discipline.NodeDecoderTests
import grind.laws.discipline.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class LongTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("Long", NodeDecoderTests.cdataEncoded[Long](_.toString).nodeDecoder)
}
