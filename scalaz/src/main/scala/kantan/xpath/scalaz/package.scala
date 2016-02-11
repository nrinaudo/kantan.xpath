package kantan.xpath

import _root_.scalaz.{Contravariant, Equal, Monad}

package object scalaz {
  /** `Monad` instance for `DecodeResult`. */
  implicit val decodeResultInstances = new Monad[DecodeResult] {
    override def bind[A, B](fa: DecodeResult[A])(f: A ⇒ DecodeResult[B]) = fa.flatMap(f)
    override def map[A, B](fa: DecodeResult[A])(f: A ⇒ B) = fa.map(f)
    override def point[A](x: ⇒ A) = DecodeResult(x)
  }

  /** `Eq` instance for `DecodeResult`. */
  implicit def decodeResultEq[A: Equal] = new Equal[DecodeResult[A]] {
    override def equal(x: DecodeResult[A], y: DecodeResult[A]): Boolean = (x, y) match {
      case (DecodeResult.Success(a), DecodeResult.Success(b)) ⇒ Equal[A].equal(a, b)
      case (DecodeResult.NotFound, DecodeResult.NotFound)     ⇒ true
      case (DecodeResult.Failure, DecodeResult.Failure)       ⇒ true
      case _                                                  ⇒ false
    }
  }

  /** `Monad` instance for `NodeDecoder`. */
  implicit val nodeDecoder = new Monad[NodeDecoder] {
    override def map[A, B](fa: NodeDecoder[A])(f: A ⇒ B) = fa.map(f)
    override def bind[A, B](fa: NodeDecoder[A])(f: A ⇒ NodeDecoder[B]) = fa.flatMap(f)
    override def point[A](x: ⇒ A) = NodeDecoder(_ ⇒ DecodeResult(x))
  }

  /** `Contravariant` instance for `XmlSource`. */
  implicit val xmlSource = new Contravariant[XmlSource] {
    override def contramap[A, B](fa: XmlSource[A])(f: B ⇒ A) = fa.contramap(f)
  }
}
