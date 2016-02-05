package grind.laws.discipline

import grind.{DecodeResult, Node, NodeDecoder}
import org.scalacheck.Arbitrary.{arbitrary => arb}
import org.scalacheck.Gen._
import org.scalacheck.{Arbitrary, Gen}

object arbitrary {
  def success[A: Arbitrary]: Gen[DecodeResult[A]] = arb[A].map(DecodeResult.success)
  implicit def arbDecodeResult[A: Arbitrary]: Arbitrary[DecodeResult[A]] =
    Arbitrary(oneOf(const(DecodeResult.failure[A]), success[A], const(DecodeResult.notFound[A])))

  implicit def arbCellDecoder[A: Arbitrary]: Arbitrary[NodeDecoder[A]] =
    Arbitrary(arb[Node => DecodeResult[A]].map(f => NodeDecoder(f)))
}