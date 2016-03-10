package kantan.xpath

import kantan.codecs.Result

object EvaluationResult {
  val NotFound: EvaluationResult[Nothing] = Result.failure(XPathError.NotFound)
  def apply[A](a: ⇒ A): EvaluationResult[A] = Result.nonFatal(a).leftMap(XPathError.DecodeError.apply)
  def success[A](a: A): EvaluationResult[A] = Result.success(a)
}
