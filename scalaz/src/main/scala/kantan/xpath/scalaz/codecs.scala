package kantan.xpath.scalaz

import kantan.xpath.{DecodeResult, NodeDecoder}

import scalaz.{Maybe, -\/, \/, \/-}

object codecs {
  implicit def or[A, B](implicit da: NodeDecoder[A], db: NodeDecoder[B]): NodeDecoder[A \/ B] = NodeDecoder { node =>
    da.decode(node).map(a => -\/(a)).orElse(db.decode(node).map(b => \/-(b)))
  }

  implicit def maybe[A](implicit da: NodeDecoder[A]): NodeDecoder[Maybe[A]] = NodeDecoder { node =>
    da.decode(node) match {
      case DecodeResult.NotFound => DecodeResult.success(Maybe.empty)
      case ra => ra.map(Maybe.just)
    }
  }
}
