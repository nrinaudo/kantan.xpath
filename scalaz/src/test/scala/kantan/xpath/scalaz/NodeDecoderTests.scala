package kantan.xpath.scalaz

import kantan.codecs.scalaz._
import kantan.codecs.laws.discipline.arbitrary._
import kantan.xpath.laws.discipline.arbitrary._
import kantan.xpath.laws.discipline.equality
import kantan.xpath.{EvaluationResult, NodeDecoder}

import scalaz.Equal
import scalaz.scalacheck.ScalazProperties.monad
import scalaz.std.anyVal._

class NodeDecoderTests extends ScalazSuite {
  implicit val nodeDecoderEq: Equal[NodeDecoder[Int]] = new Equal[NodeDecoder[Int]] {
    override def equal(a1: NodeDecoder[Int], a2: NodeDecoder[Int]): Boolean =
      equality.nodeDecoder(a1, a2)(_.toString)(Equal[EvaluationResult[Int]].equal)
  }

  checkAll("NodeDecoder", monad.laws[NodeDecoder])
}
