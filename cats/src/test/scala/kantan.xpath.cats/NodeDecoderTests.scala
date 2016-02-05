package kantan.xpath.cats

import _root_.cats.laws.discipline.MonadTests
import kantan.xpath.NodeDecoder
import eqs._
import cats.std.int._
import kantan.xpath.NodeDecoder
import kantan.xpath.laws.discipline.discipline.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class NodeDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("NodeDecoder[Int]", MonadTests[NodeDecoder].monad[Int, Int, Int])
}
