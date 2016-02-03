package grind.laws.discipline

import java.io.File
import java.util.UUID

import grind.DecodeResult.Success
import grind.laws.IllegalValue
import grind.{DecodeResult, Node, NodeDecoder}
import org.scalacheck.Arbitrary.{arbitrary => arb}
import org.scalacheck.Gen._
import org.scalacheck.{Arbitrary, Gen}

object arbitrary {
  // TODO: maybe this should be moved to a different project, to be shared with tabulate?
  val genFile: Gen[File] = for(suffix <- Gen.listOfN(3, Gen.alphaChar)) yield {
    val file = File.createTempFile("scalacheck", suffix.mkString)
    file.deleteOnExit()
    file
  }

  def success[A: Arbitrary]: Gen[DecodeResult[A]] = arb[A].map(DecodeResult.success)
  implicit def arbDecodeResult[A: Arbitrary]: Arbitrary[DecodeResult[A]] =
    Arbitrary(oneOf(const(DecodeResult.failure[A]), success[A], const(DecodeResult.notFound[A])))

  implicit def arbCellDecoder[A: Arbitrary]: Arbitrary[NodeDecoder[A]] =
    Arbitrary(arb[Node => DecodeResult[A]].map(f => NodeDecoder(f)))


  private def illegalNum[A]: Arbitrary[IllegalValue[A]] = Arbitrary(Gen.alphaStr.map(IllegalValue.apply))

  implicit val arbIllegalBigDecimal: Arbitrary[IllegalValue[BigDecimal]] = illegalNum
  implicit val arbIllegalBigInt: Arbitrary[IllegalValue[BigInt]] = illegalNum
  implicit val arbIllegalByte: Arbitrary[IllegalValue[Byte]] = illegalNum
  implicit val arbIllegalDouble: Arbitrary[IllegalValue[Double]] = illegalNum
  implicit val arbIllegalFloat: Arbitrary[IllegalValue[Float]] = illegalNum
  implicit val arbIllegalInt: Arbitrary[IllegalValue[Int]] = illegalNum
  implicit val arbIllegalLong: Arbitrary[IllegalValue[Long]] = illegalNum
  implicit val arbIllegalShort: Arbitrary[IllegalValue[Short]] = illegalNum
  implicit val arbIllegalBoolean: Arbitrary[IllegalValue[Boolean]] = Arbitrary(Gen.alphaStr.suchThat { s =>
    val l = s.toLowerCase
    l != "true" && l != "false"
  }.map(IllegalValue.apply))
  implicit val arbIllegalChar: Arbitrary[IllegalValue[Char]] = Arbitrary(Gen.alphaStr.suchThat(_.length != 1).map(IllegalValue.apply))
  implicit val arbIllegalUUID: Arbitrary[IllegalValue[UUID]] = illegalNum
}
