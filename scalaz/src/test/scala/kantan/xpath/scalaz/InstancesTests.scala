package kantan.xpath.scalaz

import kantan.xpath.XPathError.{EvaluationError, LoadingError}
import kantan.xpath.laws.discipline.arbitrary._
import kantan.xpath.{NodeDecoder, XPathError}
import equality._

import scalaz.scalacheck.ScalazProperties.{equal, functor}
import scalaz.std.anyVal._

class InstancesTests extends ScalazSuite {
  checkAll("XPathError", equal.laws[XPathError])
  checkAll("EvaluationError", equal.laws[EvaluationError])
  checkAll("LoadingError", equal.laws[LoadingError])
  checkAll("NodeDecoder[Int]", functor.laws[NodeDecoder])
}
