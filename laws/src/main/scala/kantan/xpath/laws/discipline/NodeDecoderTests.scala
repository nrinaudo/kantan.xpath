package kantan.xpath.laws.discipline

import kantan.codecs.laws.CodecValue.LegalValue
import kantan.codecs.laws.DecoderLaws
import kantan.codecs.laws.discipline.DecoderTests
import kantan.xpath.XPathError.EvaluationError
import kantan.xpath.laws.discipline.arbitrary._
import kantan.xpath.{Node, NodeDecoder, XPathError}
import org.scalacheck.Arbitrary

object NodeDecoderTests {
  def apply[A](implicit la: DecoderLaws[Node, A, EvaluationError, NodeDecoder], al: Arbitrary[LegalValue[Node, A]]): NodeDecoderTests[A] =
    DecoderTests[Node, A, XPathError.EvaluationError, NodeDecoder]
}