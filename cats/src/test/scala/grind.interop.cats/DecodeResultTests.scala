package grind.interop.cats

import _root_.cats.laws.discipline.MonadTests
import cats.std.int._
import grind.DecodeResult
import grind.interop.cats.eqs._
import grind.laws.discipline.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class DecodeResultTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("DecodeResult[Int]", MonadTests[DecodeResult].monad[Int, Int, Int])
}