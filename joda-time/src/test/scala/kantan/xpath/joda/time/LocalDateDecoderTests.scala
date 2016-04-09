package kantan.xpath.joda.time

import kantan.codecs.strings.joda.time.laws.discipline.arbitrary._
import kantan.xpath.laws.discipline.NodeDecoderTests
import kantan.xpath.laws.discipline.arbitrary._
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class LocalDateDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val formatter = DateTimeFormat.mediumDate()

  checkAll("NodeDecoder[LocalDate]", NodeDecoderTests[LocalDate].decoder[Int, Int])
}
