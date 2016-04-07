package kantan.xpath

import _root_.cats.Eq
import _root_.cats.functor.Contravariant
import kantan.codecs.cats.CatsInstances

package object cats extends CatsInstances {
  /** `Eq` instance for errors. */
  implicit val readErrorEq = new Eq[ReadError] {
    override def eqv(x: ReadError, y: ReadError) = x == y
  }

  implicit val decodeErrorEq = new Eq[DecodeError] {
    override def eqv(x: DecodeError, y: DecodeError) = x == y
  }

  implicit val parseErrorEq = new Eq[ParseError] {
    override def eqv(x: ParseError, y: ParseError) = x == y
  }

  /** `Contravariant` instance for `XmlSource`. */
  implicit val xmlSource = new Contravariant[XmlSource] {
    override def contramap[A, B](fa: XmlSource[A])(f: B ⇒ A) = fa.contramap(f)
  }
}
