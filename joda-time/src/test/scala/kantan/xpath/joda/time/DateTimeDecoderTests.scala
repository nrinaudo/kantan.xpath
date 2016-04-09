package kantan.xpath.joda.time

import kantan.codecs.strings.joda.time.laws.discipline.arbitrary._
import kantan.xpath.laws.discipline.NodeDecoderTests
import kantan.xpath.laws.discipline.arbitrary._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class DateTimeDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val formatter = ISODateTimeFormat.dateTime

  checkAll("NodeDecoder[DateTime]", NodeDecoderTests[DateTime].decoder[Int, Int])
}
