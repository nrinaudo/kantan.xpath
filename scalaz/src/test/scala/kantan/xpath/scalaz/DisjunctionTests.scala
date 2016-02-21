package kantan.xpath.scalaz

import codecs._
import kantan.codecs.laws.{IllegalString, LegalString}
import kantan.xpath.laws.discipline.arbitrary._
import kantan.xpath.laws.discipline.{NodeDecoderTests => NDTests}
import org.scalacheck.Arbitrary
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

import scalaz.\/

class DisjunctionTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  // TODO: should this be moved into a kantan.codecs-laws-cats project? I'm bound to need to re-use it.
  implicit def arbLegalDisjunction(implicit a: Arbitrary[LegalString[Either[Int, Boolean]]]): Arbitrary[LegalString[Int \/ Boolean]] =
    Arbitrary(a.arbitrary.map(_.mapDecoded(v ⇒ \/.fromEither(v))))

  implicit def arbIllegalDisjunction(implicit a: Arbitrary[IllegalString[Either[Int, Boolean]]]): Arbitrary[IllegalString[Int \/ Boolean]] =
    Arbitrary(a.arbitrary.map(_.mapDecoded(v ⇒ \/.fromEither(v))))

  checkAll("NodeDecoder[Int \\/ Boolean]", NDTests[Int \/ Boolean].decoder[Int, Int])
}

