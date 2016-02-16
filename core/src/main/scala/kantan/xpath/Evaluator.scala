package kantan.xpath

import scala.collection.generic.CanBuildFrom

/** Evaluates an expression on a given node.
  *
  * This is a bit of a crutch and should not really be used directly. Its sole purpose is to allow compound decoders
  * to work for lists and other collection types.
  */
trait Evaluator[A] {
  def evaluate(exp: Expression, node: Node): EvaluationResult[A]
}

object Evaluator {
  implicit def collection[A: NodeDecoder, F[_]](implicit cbf: CanBuildFrom[Nothing, A, F[A]]): Evaluator[F[A]] = new Evaluator[F[A]] {
    override def evaluate(exp: Expression, node: Node) = exp.all[F, A](node)
  }

  implicit def node[A: NodeDecoder]: Evaluator[A] = new Evaluator[A] {
    override def evaluate(exp: Expression, node: Node): EvaluationResult[A] = exp.first(node)
  }
}
