package grind.interop.cats

import _root_.cats.Eq
import eqs._
import _root_.cats.laws.discipline.MonadTests
import grind.DecodeResult
import grind.laws.discipline.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import cats.std.int._
import org.typelevel.discipline.scalatest.Discipline

class DecodeResultTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("DecodeResult[Int]", MonadTests[DecodeResult].monad[Int, Int, Int])
}