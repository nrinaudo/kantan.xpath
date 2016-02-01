package grind

import java.util.UUID

import grind.laws.discipline.NodeDecoderTests
import org.scalacheck.{Gen, Arbitrary}
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class UUIDTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val arbUUID = Arbitrary(Gen.uuid)

  checkAll("UUID", NodeDecoderTests.cdataEncoded[UUID](_.toString).nodeDecoder)
}
