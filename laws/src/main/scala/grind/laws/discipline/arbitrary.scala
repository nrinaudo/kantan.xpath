package grind.laws.discipline

import java.util.UUID

import org.scalacheck.Arbitrary.{arbitrary => arb}
import org.scalacheck.Gen._
import org.scalacheck.{Arbitrary, Gen}
import grind.DecodeResult
import grind.laws.IllegalValue

object arbitrary {
  def success[A: Arbitrary]: Gen[DecodeResult[A]] = arb[A].map(DecodeResult.success)
  implicit def arbDecodeResult[A: Arbitrary]: Arbitrary[DecodeResult[A]] =
    Arbitrary(oneOf(const(DecodeResult.failure[A]), success[A], const(DecodeResult.notFound[A])))



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
