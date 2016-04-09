package kantan.xpath

import java.text.SimpleDateFormat
import java.util.{Date, Locale}
import kantan.xpath.laws.discipline.NodeDecoderTests
import kantan.xpath.laws.discipline.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class DateDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)

  checkAll("NodeDecoder[Date]", NodeDecoderTests[Date].decoder[Int, Int])
}
