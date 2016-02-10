package kantan.xpath.cats

import cats.data.Xor
import kantan.xpath.NodeDecoder

object codecs {
  implicit def xor[A, B](implicit da: NodeDecoder[A], db: NodeDecoder[B]): NodeDecoder[Xor[A, B]] = NodeDecoder { node =>
    da.decode(node).map(a => Xor.left(a)).orElse(db.decode(node).map(b => Xor.right(b)))
  }
}
