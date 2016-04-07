package kantan.xpath

import _root_.scalaz._
import kantan.codecs.scalaz.ScalazInstances

package object scalaz extends ScalazInstances {
  implicit val readErrorEqual = new Equal[ReadError] {
    override def equal(a1: ReadError, a2: ReadError) = a1 == a2
  }

  implicit val decodeErrorEqual = new Equal[DecodeError] {
    override def equal(x: DecodeError, y: DecodeError) = x == y
  }

  implicit val loadingErrorEqual = new Equal[ParseError] {
    override def equal(x: ParseError, y: ParseError) = x == y
  }

  /** `Contravariant` instance for `XmlSource`. */
  implicit val xmlSource = new Contravariant[XmlSource] {
    override def contramap[A, B](fa: XmlSource[A])(f: B ⇒ A) = fa.contramap(f)
  }
}
