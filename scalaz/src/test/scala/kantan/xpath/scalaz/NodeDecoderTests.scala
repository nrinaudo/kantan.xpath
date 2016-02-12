package kantan.xpath.scalaz

import kantan.xpath.laws.discipline.{equality, arbitrary}
import kantan.xpath.{NodeDecoder, DecodeResult}
import arbitrary._

import scalaz.Equal
import scalaz.scalacheck.ScalazProperties.monad
import scalaz.std.anyVal._

class NodeDecoderTests extends ScalazSuite {
  implicit val nodeDecoderEq: Equal[NodeDecoder[Int]] = new Equal[NodeDecoder[Int]] {
    override def equal(a1: NodeDecoder[Int], a2: NodeDecoder[Int]): Boolean =
      equality.nodeDecoder(a1, a2)(_.toString)(Equal[DecodeResult[Int]].equal)
  }

  checkAll("NodeDecoder", monad.laws[NodeDecoder])
}
