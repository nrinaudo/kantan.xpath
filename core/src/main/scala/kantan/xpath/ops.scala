package kantan.xpath

import scala.collection.generic.CanBuildFrom

object ops {
  implicit class RichSource[A](val a: A) extends AnyVal {
    def asUnsafeNode(implicit source: XmlSource[A]): Node = source.asUnsafeNode(a)
    def asNode(implicit source: XmlSource[A]): LoadingResult = source.asNode(a)
    def first[B: NodeDecoder](expr: Expression)(implicit source: XmlSource[A]): XPathResult[B] = source.first(a, expr)
    def all[F[_], B: NodeDecoder](expr: Expression)
                                 (implicit s: XmlSource[A], cbf: CanBuildFrom[Nothing, B, F[B]]): XPathResult[F[B]] =
      s.all(a, expr)
  }

  implicit class RichString(val str: String) extends AnyVal {
    def xpath(implicit comp: XPathCompiler): Expression =
      Expression(str)(comp).getOrElse(sys.error(s"Not a valid XPath expression: '$str'."))
  }
}
