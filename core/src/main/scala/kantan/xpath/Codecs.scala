package kantan.xpath

import java.net.{URI, URL}
import kantan.codecs.strings.StringDecoder
import kantan.xpath.XPathError.DecodeError

object codecs {
  implicit val node: NodeDecoder[Node] = NodeDecoder(n ⇒ EvaluationResult.success(n))
  implicit val element: NodeDecoder[Element] = NodeDecoder(n ⇒ EvaluationResult.success(n.asInstanceOf[Element]))
  implicit val attr: NodeDecoder[Attr] = NodeDecoder(n ⇒ EvaluationResult.success(n.asInstanceOf[Attr]))

  implicit def fromString[A](implicit da: StringDecoder[A]): NodeDecoder[A] =
    da.tag[codecs.type].contramapEncoded((n: Node) ⇒ n.getTextContent).mapError(DecodeError.apply)
}
