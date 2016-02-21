package kantan.xpath.cats

import codecs._
import cats.data.Xor
import kantan.codecs.laws._
import kantan.xpath.laws.discipline.arbitrary._
import kantan.xpath.laws.discipline.{NodeDecoderTests => NDTests}
import org.scalacheck.Arbitrary
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class XorTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  // TODO: should this be moved into a kantan.codecs-laws-cats project? I'm bound to need to re-use it.
  implicit def arbLegalXor(implicit a: Arbitrary[LegalString[Either[Int, Boolean]]]): Arbitrary[LegalString[Int Xor Boolean]] =
    Arbitrary(a.arbitrary.map(_.mapDecoded(v ⇒ Xor.fromEither(v))))

  implicit def arbIllegalXor(implicit a: Arbitrary[IllegalString[Either[Int, Boolean]]]): Arbitrary[IllegalString[Int Xor Boolean]] =
    Arbitrary(a.arbitrary.map(_.mapDecoded(v ⇒ Xor.fromEither(v))))


  checkAll("NodeDecoder[Int Xor Boolean]", NDTests[Int Xor Boolean]
    .decoder[Int, Int])
}
