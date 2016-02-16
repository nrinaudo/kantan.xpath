package kantan.xpath.laws.discipline

import kantan.codecs.DecodeResult
import kantan.codecs.laws.discipline.arbitrary._
import kantan.xpath._
import org.scalacheck.Arbitrary.{arbitrary ⇒ arb}
import org.scalacheck.Gen._
import org.scalacheck.{Arbitrary, Gen}

object arbitrary {
  // - Arbitrary errors ------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit val arbEvaluationError: Arbitrary[XPathError.EvaluationError] =
    Arbitrary(oneOf(const(XPathError.NotFound), const(kantan.xpath.XPathError.DecodeError(new Exception))))
  implicit val arbLoadingError: Arbitrary[XPathError.LoadingError] =
    Arbitrary(oneOf(const(XPathError.ParseError(new Exception())), const(XPathError.IOError(new Exception()))))
  implicit val arbXPathError: Arbitrary[XPathError] =
    Arbitrary(oneOf(arb[XPathError.EvaluationError], arb[XPathError.LoadingError]))



  // - Arbitrary results -----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit def arbEvaluationResult[A: Arbitrary]: Arbitrary[EvaluationResult[A]] =
    Arbitrary(oneOf(arb[DecodeResult.Failure[XPathError.EvaluationError]], arb[DecodeResult.Success[A]]))
    implicit def arbXPathResult[A: Arbitrary]: Arbitrary[XPathResult[A]] =
    Arbitrary(oneOf(arb[DecodeResult.Failure[XPathError]], arb[DecodeResult.Success[A]]))



  // - Misc. arbitraries -----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit def arbCellDecoder[A: Arbitrary]: Arbitrary[NodeDecoder[A]] =
    Arbitrary(arb[Node ⇒ EvaluationResult[A]].map(f ⇒ NodeDecoder(f)))

  implicit def arbTuple1[A: Arbitrary]: Arbitrary[Tuple1[A]] =
    Arbitrary(arb[A].map(a ⇒ Tuple1(a)))
}
