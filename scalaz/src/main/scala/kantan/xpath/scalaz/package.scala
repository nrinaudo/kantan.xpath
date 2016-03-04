package kantan.xpath

import kantan.codecs.scalaz.ScalazInstances
import kantan.xpath.XPathError.{EvaluationError, LoadingError}

import _root_.scalaz._

package object scalaz extends ScalazInstances {
  implicit def or[A, B](implicit da: NodeDecoder[A], db: NodeDecoder[B]): NodeDecoder[A \/ B] = NodeDecoder { node ⇒
    da.decode(node).map(a ⇒ -\/(a)).orElse(db.decode(node).map(b ⇒ \/-(b)))
  }

  implicit val errorEqual = new Equal[XPathError] {
    override def equal(a1: XPathError, a2: XPathError) = a1 == a2
  }

  implicit val evaluationErrorEqual = new Equal[XPathError.EvaluationError] {
    override def equal(x: EvaluationError, y: EvaluationError) = x == y
  }

  implicit val loadingErrorEqual = new Equal[XPathError.LoadingError] {
    override def equal(x: LoadingError, y: LoadingError) = x == y
  }

  /** `Contravariant` instance for `XmlSource`. */
  implicit val xmlSource = new Contravariant[XmlSource] {
    override def contramap[A, B](fa: XmlSource[A])(f: B ⇒ A) = fa.contramap(f)
  }
}
