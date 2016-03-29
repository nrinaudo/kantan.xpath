package kantan.xpath.scalaz

import kantan.xpath.XPathError
import kantan.xpath.XPathError.{EvaluationError, LoadingError}
import kantan.xpath.laws.discipline.arbitrary._
import scalaz.scalacheck.ScalazProperties.equal

class InstancesTests extends ScalazSuite {
  checkAll("XPathError", equal.laws[XPathError])
  checkAll("EvaluationError", equal.laws[EvaluationError])
  checkAll("LoadingError", equal.laws[LoadingError])
}
