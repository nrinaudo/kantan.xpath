package kantan.xpath.cats

import _root_.cats.laws.discipline.FunctorTests
import kantan.xpath.NodeDecoder
import kantan.xpath.cats.eqs._
import kantan.xpath.laws.discipline.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class NodeDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("NodeDecoder[Int]", FunctorTests[NodeDecoder].functor[Int, Int, Int])
}
