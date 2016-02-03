package grind.interop.cats

import _root_.cats.laws.discipline.MonadTests
import algebra.Eq
import cats.std.int._
import grind.laws.discipline.arbitrary._
import grind.laws.discipline.equality
import grind.{DecodeResult, NodeDecoder}
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class NodeDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val eq: Eq[NodeDecoder[Int]] = new Eq[NodeDecoder[Int]] {
    override def eqv(x: NodeDecoder[Int], y: NodeDecoder[Int]): Boolean = equality.nodeDecoder(x, y)(_.toString)(Eq[DecodeResult[Int]].eqv)
  }

  checkAll("NodeDecoderTests[Int]", MonadTests[NodeDecoder].monad[Int, Int, Int])
}
