package kantan.xpath

import kantan.xpath.laws.discipline.discipline.NodeDecoderTests
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class EitherTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("NodeDecoder[Either[Int, Boolean]]", NodeDecoderTests.cdataEncoded[Either[Int, Boolean]] {
    case Left(i)  ⇒ i.toString
    case Right(b) ⇒ b.toString
  }.nodeDecoder)
}
