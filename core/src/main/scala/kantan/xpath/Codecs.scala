package kantan.xpath

import kantan.codecs.strings.StringDecoder
import kantan.xpath.XPathError.DecodeError

object Codecs {
  implicit val node: NodeDecoder[Node] = NodeDecoder(n ⇒ EvaluationResult.success(n))
  implicit val element: NodeDecoder[Element] = NodeDecoder(n ⇒ EvaluationResult.success(n.asInstanceOf[Element]))
  implicit val attr: NodeDecoder[Attr] = NodeDecoder(n ⇒ EvaluationResult.success(n.asInstanceOf[Attr]))

  implicit def fromString[A](implicit da: StringDecoder[A]): NodeDecoder[A] =
    da.tag[Codecs.type].contramapEncoded((n: Node) ⇒ n.getTextContent).mapError(DecodeError.apply)
}
