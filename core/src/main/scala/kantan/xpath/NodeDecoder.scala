package kantan.xpath

import kantan.codecs.Decoder

object NodeDecoder extends GeneratedDecoders {
  implicit def apply[A](implicit da: NodeDecoder[A]): NodeDecoder[A] = da

  def apply[A](f: Node ⇒ EvaluationResult[A]): NodeDecoder[A] = Decoder(f)
}
