package kantan.xpath.cats

import cats.data.Xor
import export.{export, exports}
import kantan.xpath.NodeDecoder

@exports
object codecs {
  @export(Orphan)
  implicit def xor[A, B](implicit da: NodeDecoder[A], db: NodeDecoder[B]): NodeDecoder[A Xor B] = NodeDecoder { node ⇒
    da.decode(node).map(a ⇒ Xor.left(a)).orElse(db.decode(node).map(b ⇒ Xor.right(b)))
  }
}
