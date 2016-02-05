package kantan.xpath.cats

import _root_.cats.laws.discipline.MonadTests
import cats.std.int._
import kantan.xpath.cats.eqs._
import kantan.xpath.laws.discipline.discipline.arbitrary._
import kantan.xpath.DecodeResult
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class DecodeResultTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("DecodeResult[Int]", MonadTests[DecodeResult].monad[Int, Int, Int])
}