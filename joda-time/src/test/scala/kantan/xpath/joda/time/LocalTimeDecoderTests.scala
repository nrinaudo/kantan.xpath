package kantan.xpath.joda.time

import kantan.codecs.strings.joda.time.laws.discipline.arbitrary._
import kantan.xpath.laws.discipline.NodeDecoderTests
import kantan.xpath.laws.discipline.arbitrary._
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class LocalTimeDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val formatter = DateTimeFormat.mediumTime()

  checkAll("NodeDecoder[LocalTime]", NodeDecoderTests[LocalTime].decoder[Int, Int])
}
