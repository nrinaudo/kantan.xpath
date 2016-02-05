package kantan.xpath

import _root_.cats.Monad
import _root_.cats.functor.Contravariant
import algebra.Eq

package object cats {
  /** `Monad` instance for `DecodeResult`. */
  implicit val decodeResultInstances = new Monad[DecodeResult] {
    override def flatMap[A, B](fa: DecodeResult[A])(f: A => DecodeResult[B]) = fa.flatMap(f)
    override def map[A, B](fa: DecodeResult[A])(f: A => B) = fa.map(f)
    override def pure[A](x: A) = DecodeResult(x)
  }

  /** `Eq` instance for `DecodeResult`. */
  implicit def decodeResultEq[A: Eq] = new Eq[DecodeResult[A]] {
    override def eqv(x: DecodeResult[A], y: DecodeResult[A]): Boolean = (x, y) match {
      case (DecodeResult.Success(a), DecodeResult.Success(b)) => Eq[A].eqv(a, b)
      case (DecodeResult.NotFound, DecodeResult.NotFound)     => true
      case (DecodeResult.Failure, DecodeResult.Failure)       => true
      case _                                                  => false
    }
  }

  /** `Monad` instance for `NodeDecoder`. */
  implicit val nodeDecoder = new Monad[NodeDecoder] {
    override def map[A, B](fa: NodeDecoder[A])(f: A => B) = fa.map(f)
    override def flatMap[A, B](fa: NodeDecoder[A])(f: A => NodeDecoder[B]) = fa.flatMap(f)
    override def pure[A](x: A) = NodeDecoder(_ => DecodeResult(x))
  }

  /** `Contravariant` instance for `XmlSource`. */
  implicit val xmlSource = new Contravariant[XmlSource] {
    override def contramap[A, B](fa: XmlSource[A])(f: B => A) = fa.contramap(f)
  }
}
