package kantan.xpath.cats

import _root_.cats._
import _root_.cats.laws.discipline.FunctorTests
import _root_.cats.std.all._
import kantan.xpath._
import kantan.xpath.laws.discipline.arbitrary._
import kantan.xpath.laws.discipline.equality
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class InstancesTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val nodeDecoderEq: Eq[NodeDecoder[Int]] = new Eq[NodeDecoder[Int]] {
    override def eqv(a1: NodeDecoder[Int], a2: NodeDecoder[Int]) =
      equality.nodeDecoder(a1, a2)(_.toString)(algebra.Eq[EvaluationResult[Int]].eqv)
  }

  checkAll("NodeDecoder", FunctorTests[NodeDecoder].functor[Int, Int, Int])
}
