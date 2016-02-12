package kantan.xpath.cats

import algebra.Eq
import algebra.std.int._
import kantan.xpath.NodeDecoder
import kantan.xpath.laws.discipline.equality
import kantan.xpath.{NodeDecoder, DecodeResult}

object eqs {
  implicit val eqi3: Eq[(Int, Int, Int)] = new Eq[(Int, Int, Int)] {
    override def eqv(x: (Int, Int, Int), y: (Int, Int, Int)): Boolean =
      x._1 == y._1 && x._2 == y._2 && x._3 == y._3
  }

  implicit val eqDecodeI3: Eq[NodeDecoder[(Int, Int, Int)]] = new Eq[NodeDecoder[(Int, Int, Int)]] {
    override def eqv(x: NodeDecoder[(Int, Int, Int)], y: NodeDecoder[(Int, Int, Int)]): Boolean = equality.nodeDecoder(x, y)(_.toString)(Eq[DecodeResult[(Int, Int, Int)]].eqv)
  }

  implicit val eqDecodeI: Eq[NodeDecoder[Int]] = new Eq[NodeDecoder[Int]] {
    override def eqv(x: NodeDecoder[Int], y: NodeDecoder[Int]): Boolean = equality.nodeDecoder(x, y)(_.toString)(Eq[DecodeResult[Int]].eqv)
  }
}
