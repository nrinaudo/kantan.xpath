package kantan.xpath

object ops extends NodeDecoder.ToNodeDecoderOps
                   with XmlSource.ToXmlSourceOps {
  implicit class RichString(val str: String) extends AnyVal {
    def xpath(implicit comp: XPathCompiler): Expression =
      Expression(str)(comp).getOrElse(sys.error(s"Not a valid XPath expression: '$str'."))
  }
}
