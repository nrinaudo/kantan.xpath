package kantan.xpath

import kantan.codecs.DecodeResult

object EvaluationResult {
  val NotFound: EvaluationResult[Nothing] = DecodeResult.failure(XPathError.NotFound)
  def apply[A](a: ⇒ A): EvaluationResult[A] = DecodeResult.nonFatal(a).leftMap(XPathError.DecodeError.apply)
}
