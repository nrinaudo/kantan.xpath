package kantan.xpath.cats

import kantan.codecs.laws.{IllegalString, LegalString}

import codecs._
import arbitrary._
import cats.data.Xor
import kantan.xpath.laws.discipline.NodeDecoderTests
import org.scalacheck.Arbitrary
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class XorTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit def arbLegalXor(implicit a: Arbitrary[LegalString[Either[Int, Boolean]]]): Arbitrary[LegalString[Int Xor Boolean]] =
    Arbitrary(a.arbitrary.map(_.mapDecoded(v ⇒ Xor.fromEither(v))))

  implicit def arbIllegalXor(implicit a: Arbitrary[IllegalString[Either[Int, Boolean]]]): Arbitrary[IllegalString[Int Xor Boolean]] =
    Arbitrary(a.arbitrary.map(_.mapDecoded(v ⇒ Xor.fromEither(v))))


  checkAll("NodeDecoder[Int Xor Boolean]", NodeDecoderTests[Int Xor Boolean].decoder[Int, Int])
}
