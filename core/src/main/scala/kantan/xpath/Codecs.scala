package kantan.xpath

import kantan.codecs.strings.StringDecoder
import kantan.xpath.DecodeError.TypeError

object codecs {
  implicit val node: NodeDecoder[Node] = NodeDecoder(n ⇒ DecodeResult.success(n))
  implicit val element: NodeDecoder[Element] = NodeDecoder(n ⇒ DecodeResult.success(n.asInstanceOf[Element]))
  implicit val attr: NodeDecoder[Attr] = NodeDecoder(n ⇒ DecodeResult.success(n.asInstanceOf[Attr]))

  implicit def fromString[A](implicit da: StringDecoder[A]): NodeDecoder[A] =
    da.tag[codecs.type].contramapEncoded((n: Node) ⇒ n.getTextContent).mapError(TypeError.apply)
}
