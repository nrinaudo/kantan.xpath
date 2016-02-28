package kantan.xpath.cats

import _root_.cats.Eq
import kantan.codecs.cats.laws._
import kantan.xpath.XPathError.EvaluationError
import kantan.xpath._
import org.scalacheck.Arbitrary

object equality {
  implicit def nodeDecoderEq[D: Arbitrary: Eq] = {
    implicit val arbNode = arbitrary.arbNode[D](_.toString)
    decoderEq[Node, D, EvaluationError, NodeDecoder]
  }
}
