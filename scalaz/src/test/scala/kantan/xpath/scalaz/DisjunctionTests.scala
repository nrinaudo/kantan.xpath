package kantan.xpath.scalaz

import kantan.codecs.laws.discipline.arbitrary
import kantan.codecs.laws.{IllegalString, LegalString}
import kantan.xpath.laws.discipline.arbitrary._
import kantan.xpath.laws.discipline.{NodeDecoderTests => NDTests}
import org.scalacheck.Arbitrary
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

import scala.util.Try
import scalaz.scalacheck.ScalazArbitrary._
import scalaz.{-\/, \/, \/-}

class DisjunctionTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val arbLegal: Arbitrary[LegalString[Int \/ Boolean]] =
    arbitrary.arbLegal(_.fold(_.toString, _.toString))
  implicit val arbIllegal: Arbitrary[IllegalString[Int \/ Boolean]] =
    arbitrary.arbIllegal { s => Try(-\/(s.toInt)).getOrElse(\/-(s.toBoolean)) }

  checkAll("NodeDecoder[Int \\/ Boolean]", NDTests[Int \/ Boolean].decoder[Int, Int])
}

