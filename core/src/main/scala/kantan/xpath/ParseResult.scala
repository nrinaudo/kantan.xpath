package kantan.xpath

import kantan.codecs.Result

object ParseResult {
  def open[A](acquire: ⇒ A)(parse: A ⇒ ParseResult): ParseResult =
    Result.nonFatal(acquire).leftMap(ParseError.IOError).flatMap(parse)

  def apply(node: ⇒ Node): ParseResult = Result.nonFatal(node).leftMap(ParseError.SyntaxError.apply)
}
