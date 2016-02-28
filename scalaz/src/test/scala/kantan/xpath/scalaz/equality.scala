package kantan.xpath.scalaz

import kantan.codecs.scalaz.laws._
import kantan.xpath.XPathError.EvaluationError
import kantan.xpath._
import org.scalacheck.Arbitrary

import _root_.scalaz.Equal

object equality {
  implicit def nodeDecoderEqual[D: Arbitrary: Equal] = {
    implicit val arbNode = arbitrary.arbNode[D](_.toString)
    decoderEqual[Node, D, EvaluationError, NodeDecoder]
  }
}
