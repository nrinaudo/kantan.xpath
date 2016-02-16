package kantan.xpath

import kantan.codecs.DecodeResult

object LoadingResult {
  def open[A](acquire: ⇒ A)(parse: A ⇒ LoadingResult): LoadingResult =
    DecodeResult.nonFatal(acquire).leftMap(XPathError.IOError).flatMap(parse)
  def apply(node: ⇒ Node): LoadingResult = DecodeResult.nonFatal(node).leftMap(XPathError.ParseError.apply)
}
