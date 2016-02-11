package kantan.xpath.laws.discipline.discipline

import kantan.xpath.{Node, NodeDecoder}
import kantan.xpath.DecodeResult
import org.scalacheck.Arbitrary.{arbitrary ⇒ arb}
import org.scalacheck.Gen._
import org.scalacheck.{Arbitrary, Gen}

object arbitrary {
  def success[A: Arbitrary]: Gen[DecodeResult[A]] = arb[A].map(DecodeResult.success)
  implicit def arbDecodeResult[A: Arbitrary]: Arbitrary[DecodeResult[A]] =
    Arbitrary(oneOf(const(DecodeResult.failure[A]), success[A], const(DecodeResult.notFound[A])))

  implicit def arbCellDecoder[A: Arbitrary]: Arbitrary[NodeDecoder[A]] =
    Arbitrary(arb[Node ⇒ DecodeResult[A]].map(f ⇒ NodeDecoder(f)))

  implicit def arbTuple1[A: Arbitrary]: Arbitrary[Tuple1[A]] =
    Arbitrary(arb[A].map(a ⇒ Tuple1(a)))
}
