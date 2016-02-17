package kantan.xpath

import kantan.codecs.Result

object LoadingResult {
  def open[A](acquire: ⇒ A)(parse: A ⇒ LoadingResult): LoadingResult =
    Result.nonFatal(acquire).leftMap(XPathError.IOError).flatMap(parse)
  def apply(node: ⇒ Node): LoadingResult = Result.nonFatal(node).leftMap(XPathError.ParseError.apply)
}
