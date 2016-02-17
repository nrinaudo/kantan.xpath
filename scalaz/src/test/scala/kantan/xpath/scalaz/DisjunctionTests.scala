package kantan.xpath.scalaz

import kantan.codecs.laws.CodecValue
import kantan.codecs.laws.CodecValue.{IllegalValue, LegalValue}
import kantan.xpath.laws.discipline.arbitrary._
import org.scalacheck.Arbitrary
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline
import kantan.xpath.laws.discipline.{NodeDecoderTests ⇒ NDTests}
import codecs._

import scala.util.Try
import scalaz.{-\/, \/, \/-}
import scalaz.scalacheck.ScalazArbitrary._

class DisjunctionTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val arbLegal: Arbitrary[LegalValue[String, Int \/ Boolean]] =
    CodecValue.arbLegal(_.fold(_.toString, _.toString))
  implicit val arbIllegal: Arbitrary[IllegalValue[String, Int \/ Boolean]] =
    CodecValue.arbIllegal { s => Try(-\/(s.toInt)).getOrElse(\/-(s.toBoolean)) }

  checkAll("NodeDecoder[Int \\/ Boolean]", NDTests[Int \/ Boolean].decoder[Int, Int])
}

