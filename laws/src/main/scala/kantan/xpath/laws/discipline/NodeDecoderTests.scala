package kantan.xpath.laws.discipline

import kantan.codecs.laws.discipline.DecoderTests
import kantan.xpath._
import kantan.xpath.laws.discipline.arbitrary._
import org.scalacheck.Arbitrary

object NodeDecoderTests {
  def apply[A](implicit la: NodeDecoderLaws[A], al: Arbitrary[LegalNode[A]]): NodeDecoderTests[A] =
    DecoderTests[Node, A, XPathError.EvaluationError, codecs.type]
}
