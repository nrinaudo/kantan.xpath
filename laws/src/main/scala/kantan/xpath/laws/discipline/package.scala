package kantan.xpath.laws

import kantan.codecs.laws.discipline.DecoderTests
import kantan.xpath._

package object discipline {
  type NodeDecoderTests[A] = DecoderTests[Node, A, XPathError.EvaluationError, NodeDecoder]
}
