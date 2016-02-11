package kantan.xpath.scalaz

import export.{export, exports}
import kantan.xpath.NodeDecoder

import scalaz.{-\/, \/, \/-}

@exports
object codecs {
  @export(Orphan)
  implicit def or[A, B](implicit da: NodeDecoder[A], db: NodeDecoder[B]): NodeDecoder[A \/ B] = NodeDecoder { node ⇒
    da.decode(node).map(a ⇒ -\/(a)).orElse(db.decode(node).map(b ⇒ \/-(b)))
  }
}
