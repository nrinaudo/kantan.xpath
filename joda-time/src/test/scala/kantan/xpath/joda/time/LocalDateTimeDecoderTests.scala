package kantan.xpath.joda.time

import kantan.codecs.strings.joda.time.laws.discipline.arbitrary._
import kantan.xpath.laws.discipline.NodeDecoderTests
import kantan.xpath.laws.discipline.arbitrary._
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class LocalDateTimeDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val formatter = DateTimeFormat.mediumDateTime()

  checkAll("NodeDecoder[LocalDateTime]", NodeDecoderTests[LocalDateTime].decoder[Int, Int])
}
