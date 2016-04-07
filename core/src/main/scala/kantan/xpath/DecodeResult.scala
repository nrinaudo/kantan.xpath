package kantan.xpath

import kantan.codecs.Result

object DecodeResult {
  val NotFound: DecodeResult[Nothing] = Result.failure(DecodeError.NotFound)
  def apply[A](a: ⇒ A): DecodeResult[A] = Result.nonFatal(a).leftMap(DecodeError.TypeError.apply)
  def success[A](a: A): DecodeResult[A] = Result.success(a)
}
