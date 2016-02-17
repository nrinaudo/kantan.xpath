package kantan.xpath.cats

import codecs._
import cats.data.Xor
import cats.laws.discipline.arbitrary._
import kantan.codecs.laws.CodecValue.{IllegalValue, LegalValue}
import kantan.codecs.laws._
import kantan.codecs.laws.discipline.arbitrary
import kantan.xpath.laws.discipline.arbitrary._
import kantan.xpath.laws.discipline.{NodeDecoderTests => NDTests}
import org.scalacheck.Arbitrary
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

import scala.util.Try

class XorTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val arbLegal: Arbitrary[LegalString[Int Xor Boolean]] =
    arbitrary.arbLegal(_.fold(_.toString, _.toString))
  implicit val arbIllegal: Arbitrary[IllegalString[Int Xor Boolean]] =
    arbitrary.arbIllegal { s => Try(Xor.left(s.toInt)).getOrElse(Xor.right(s.toBoolean)) }

  checkAll("NodeDecoder[Int Xor Boolean]", NDTests[Int Xor Boolean].decoder[Int, Int])
}
