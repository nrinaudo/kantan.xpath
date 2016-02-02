package grind

import grind.laws.discipline.SafeNodeDecoderTests
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class StringTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("String", SafeNodeDecoderTests.cdataEncoded[String](_.toString).safeNodeDecoder)
}
