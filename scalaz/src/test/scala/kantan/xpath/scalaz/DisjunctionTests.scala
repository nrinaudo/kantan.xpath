package kantan.xpath.scalaz

import kantan.xpath.laws.discipline.discipline.{NodeDecoderTests => NDTests}
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline
import codecs._

import scalaz.{-\/, \/, \/-}
import scalaz.scalacheck.ScalazArbitrary._

class DisjunctionTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("NodeDecoder[Int \\/ Boolean]", NDTests.cdataEncoded[Int \/ Boolean] {
    case -\/(i) => i.toString
    case \/-(b) => b.toString
  }.nodeDecoder)
}

