package kantan.xpath.laws

import kantan.codecs.laws.CodecValue.{IllegalValue, LegalValue}
import kantan.codecs.laws.DecoderLaws
import kantan.codecs.laws.discipline.DecoderTests
import kantan.xpath._

package object discipline {
  type LegalNode[A] = LegalValue[Node, A]
  type IllegalNode[A] = IllegalValue[Node, A]

  type NodeDecoderLaws[A] = DecoderLaws[Node, A, XPathError.EvaluationError, Codecs.type]
  type NodeDecoderTests[A] = DecoderTests[Node, A, XPathError.EvaluationError, Codecs.type]
}
