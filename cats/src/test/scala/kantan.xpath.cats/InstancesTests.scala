package kantan.xpath.cats

import _root_.cats.laws.discipline.FunctorTests
import _root_.cats.std.all._
import kantan.xpath._
import kantan.xpath.cats.equality._
import kantan.xpath.laws.discipline.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class InstancesTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("NodeDecoder", FunctorTests[NodeDecoder].functor[Int, Int, Int])
}
