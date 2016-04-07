package kantan.xpath.laws.discipline

import kantan.codecs.Result
import kantan.codecs.laws._
import kantan.xpath._
import kantan.xpath.ops._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.{arbitrary => arb}
import org.scalacheck.Gen._

object arbitrary extends ArbitraryInstances

trait ArbitraryInstances extends kantan.codecs.laws.discipline.ArbitraryInstances
                                 with kantan.xpath.laws.discipline.ArbitraryArities {
  // - Arbitrary errors ------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit val arbDecodeError: Arbitrary[DecodeError] =
    Arbitrary(oneOf(const(DecodeError.NotFound), const(DecodeError.TypeError(new Exception))))
  implicit val arbParseError: Arbitrary[ParseError] =
    Arbitrary(oneOf(const(ParseError.SyntaxError(new Exception())), const(ParseError.IOError(new Exception()))))
  implicit val arbXPathError: Arbitrary[ReadError] =
    Arbitrary(oneOf(arb[DecodeError], arb[ParseError]))



  // - Arbitrary results -----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit def arbDecodeResult[A: Arbitrary]: Arbitrary[DecodeResult[A]] =
    Arbitrary(oneOf(arb[Result.Failure[DecodeError]], arb[Result.Success[A]]))
  implicit def arbXPathResult[A: Arbitrary]: Arbitrary[ReadResult[A]] =
    Arbitrary(oneOf(arb[Result.Failure[ReadError]], arb[Result.Success[A]]))



  // - Arbitrary values ------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  private def asCDataNode(value: String): Node = {
    val n = s"<element></element>".asUnsafeNode.getFirstChild.asInstanceOf[Element]
    n.setTextContent(value)
    n
  }

  implicit def arbLegalNode[A](implicit la: Arbitrary[LegalString[A]]): Arbitrary[LegalNode[A]] =
    Arbitrary(la.arbitrary.map(_.mapEncoded(asCDataNode)))
  implicit def arbIllegalNode[A](implicit ia: Arbitrary[IllegalString[A]]): Arbitrary[IllegalNode[A]] =
    Arbitrary(ia.arbitrary.map(_.mapEncoded(asCDataNode)))


  // - Misc. arbitraries -----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  def arbNode[A: Arbitrary](f: A ⇒ String): Arbitrary[Node] =
    Arbitrary(Arbitrary.arbitrary[A].map(a ⇒ s"<root>${f(a)}</root>".asUnsafeNode))

  implicit def arbCellDecoder[A: Arbitrary]: Arbitrary[NodeDecoder[A]] =
    Arbitrary(arb[Node ⇒ DecodeResult[A]].map(f ⇒ NodeDecoder(f)))

  implicit def arbTuple1[A: Arbitrary]: Arbitrary[Tuple1[A]] =
    Arbitrary(arb[A].map(a ⇒ Tuple1(a)))
}
