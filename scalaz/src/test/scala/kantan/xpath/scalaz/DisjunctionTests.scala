package kantan.xpath.scalaz

import arbitrary._
import kantan.xpath.laws.discipline.NodeDecoderTests
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

import scalaz.\/

class DisjunctionTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val legalDisjunction = arbLegalNode[Int \/ Boolean]
  implicit val illegalDisjunction = arbIllegalNode[Int \/ Boolean]

  checkAll("NodeDecoder[Int \\/ Boolean]", NodeDecoderTests[Int \/ Boolean].decoder[Int, Int])
}

