package grind

import grind.laws.discipline.NodeDecoderTests
import grind.laws.discipline.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class IntTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("Int", NodeDecoderTests.cdataEncoded[Int](_.toString).nodeDecoder)
}
