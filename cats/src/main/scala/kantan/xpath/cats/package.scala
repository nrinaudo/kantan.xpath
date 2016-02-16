package kantan.xpath

import _root_.cats.functor.Contravariant
import _root_.cats.{Eq, Monad}
import kantan.codecs.DecodeResult
import kantan.codecs.cats.CatsInstances
import kantan.xpath.XPathError.{LoadingError, EvaluationError}

package object cats extends CatsInstances {
  /** `Eq` instance for errors. */
  implicit val errorEq = new Eq[XPathError] {
    override def eqv(x: XPathError, y: XPathError) = x == y
  }

  implicit val evaluationErrorEq = new Eq[XPathError.EvaluationError] {
    override def eqv(x: EvaluationError, y: EvaluationError) = x == y
  }

  implicit val loadingErrorEq = new Eq[XPathError.LoadingError] {
    override def eqv(x: LoadingError, y: LoadingError) = x == y
  }

  /** `Monad` instance for `NodeDecoder`. */
  implicit val nodeDecoder = new Monad[NodeDecoder] {
    override def map[A, B](fa: NodeDecoder[A])(f: A ⇒ B) = fa.map(f)
    override def flatMap[A, B](fa: NodeDecoder[A])(f: A ⇒ NodeDecoder[B]) = fa.flatMap(f)
    override def pure[A](x: A) = NodeDecoder(_ ⇒ DecodeResult.success(x))
  }

  /** `Contravariant` instance for `XmlSource`. */
  implicit val xmlSource = new Contravariant[XmlSource] {
    override def contramap[A, B](fa: XmlSource[A])(f: B ⇒ A) = fa.contramap(f)
  }
}
