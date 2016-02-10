package kantan.xpath.scalaz

import export.{export, exports}
import kantan.xpath.{DecodeResult, NodeDecoder}

import scalaz.{Maybe, -\/, \/, \/-}

@exports
object codecs {
  @export(Orphan)
  implicit def or[A, B](implicit da: NodeDecoder[A], db: NodeDecoder[B]): NodeDecoder[A \/ B] = NodeDecoder { node =>
    da.decode(node).map(a => -\/(a)).orElse(db.decode(node).map(b => \/-(b)))
  }

  @export(Orphan)
  implicit def maybe[A](implicit da: NodeDecoder[A]): NodeDecoder[Maybe[A]] = NodeDecoder { node =>
    da.decode(node) match {
      case DecodeResult.NotFound => DecodeResult.success(Maybe.empty)
      case ra => ra.map(Maybe.just)
    }
  }
}
