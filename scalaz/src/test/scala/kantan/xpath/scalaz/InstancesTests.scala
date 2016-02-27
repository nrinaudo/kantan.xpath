package kantan.xpath.scalaz

import kantan.xpath.XPathError.{EvaluationError, LoadingError}
import kantan.xpath.laws.discipline.arbitrary._
import kantan.xpath.laws.discipline.equality
import kantan.xpath.{EvaluationResult, NodeDecoder, XPathError}

import scalaz.Equal
import scalaz.scalacheck.ScalazProperties.{equal, functor}
import scalaz.std.anyVal._

class InstancesTests extends ScalazSuite {
  implicit val nodeDecoderEq: Equal[NodeDecoder[Int]] = new Equal[NodeDecoder[Int]] {
    override def equal(a1: NodeDecoder[Int], a2: NodeDecoder[Int]): Boolean =
      equality.nodeDecoder(a1, a2)(_.toString)(Equal[EvaluationResult[Int]].equal)
  }

  checkAll("XPathError", equal.laws[XPathError])
  checkAll("EvaluationError", equal.laws[EvaluationError])
  checkAll("LoadingError", equal.laws[LoadingError])
  checkAll("NodeDecoder[Int]", functor.laws[NodeDecoder])
}
