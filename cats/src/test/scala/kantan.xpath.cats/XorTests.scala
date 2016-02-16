package kantan.xpath.cats

import codecs._
import cats.data.Xor
import cats.laws.discipline.arbitrary._
import kantan.xpath.laws.discipline.{NodeDecoderTests ⇒ NDTests}
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class XorTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("NodeDecoder[Xor[Int, Boolean]]", NDTests.cdataEncoded[Xor[Int, Boolean]] {
    case Xor.Left(i)  ⇒ i.toString
    case Xor.Right(b) ⇒ b.toString
  }.nodeDecoder)
}
