package kantan.xpath.cats

import cats.laws.discipline.MonadTests
import cats.std.int._
import kantan.xpath.NodeDecoder
import kantan.xpath.cats.eqs._
import kantan.xpath.laws.discipline.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class NodeDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("NodeDecoder[Int]", MonadTests[NodeDecoder].monad[Int, Int, Int])
}
