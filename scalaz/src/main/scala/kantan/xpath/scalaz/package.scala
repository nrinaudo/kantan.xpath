package kantan.xpath

import _root_.scalaz._
import kantan.codecs.scalaz.ScalazInstances
import kantan.xpath.XPathError.{EvaluationError, LoadingError}

package object scalaz extends ScalazInstances {
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
