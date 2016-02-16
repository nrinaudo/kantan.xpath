package kantan.xpath

import javax.xml.xpath.{XPathConstants, XPathExpression}

import kantan.codecs.DecodeResult

import scala.annotation.tailrec
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable

class Expression private[xpath] (val expr: XPathExpression) {
  /** Finds the first node that matches the expression and evaluates it as an `A`. */
  def first[A](n: Node)(implicit da: NodeDecoder[A]): EvaluationResult[A] = {
    val res = expr.evaluate(n, XPathConstants.NODE).asInstanceOf[Node]
    if(res == null) EvaluationResult.NotFound
    else            da.decode(res)
  }

  /** Finds all nodes matching the expression and evaluate them as an `F[A]]`, where `F` is a collection class. */
  def every[F[_], A](n: Node)(implicit da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, EvaluationResult[A], F[EvaluationResult[A]]]): F[EvaluationResult[A]] = {
    val res = expr.evaluate(n, XPathConstants.NODESET).asInstanceOf[NodeList]
    val out = cbf()
    for(i ← 0 until res.getLength) out += da.decode(res.item(i))
    out.result()
  }

  def all[F[_], A](n: Node)(implicit da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, A, F[A]]): EvaluationResult[F[A]] = {
    val res = expr.evaluate(n, XPathConstants.NODESET).asInstanceOf[NodeList]

    @tailrec
    def loop(i: Int, acc: mutable.Builder[A, F[A]]): EvaluationResult[F[A]] =
      if(i >= res.getLength) DecodeResult.success(acc.result())
      else da.decode(res.item(i)) match {
        case DecodeResult.Success(a) ⇒
          acc += a
          loop(i + 1, acc)
        case f@DecodeResult.Failure(_) ⇒ f
      }

    loop(0, cbf())
  }

  /** Turns this expression into an unsafe one
    *
    * An unsafe expression is one that throws exception when error occurs, as opposed to wrapping them in a
    * [[DecodeResult.Failure]].
    */
  def unsafe: UnsafeExpression = new UnsafeExpression(this)

  def liftFirst[A: NodeDecoder]: (Node ⇒ EvaluationResult[A]) = n ⇒ first(n)
  def liftAll[F[_], A: NodeDecoder](implicit cbf: CanBuildFrom[Nothing, A, F[A]]): (Node ⇒ EvaluationResult[F[A]]) =
    n ⇒ all(n)

  def liftEvery[F[_], A](implicit da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, EvaluationResult[A], F[EvaluationResult[A]]]): (Node ⇒ F[EvaluationResult[A]]) =
    n ⇒ every(n)
}

class UnsafeExpression private[xpath] (val expr: Expression) {
  def first[A: NodeDecoder](n: Node): A = expr.first(n).get
  def all[F[_], A](n: Node)(implicit da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, A, F[A]]): F[A] =
    expr.all[F, A](n).get
  def liftFirst[A: NodeDecoder]: (Node ⇒ A) = n ⇒ first(n)
  def liftAll[F[_], A: NodeDecoder](implicit cbf: CanBuildFrom[Nothing, A, F[A]]): (Node ⇒ F[A]) = n ⇒ all(n)
  def safe: Expression = expr
}

object Expression {
  def apply(str: String)(implicit compiler: XPathCompiler): Option[Expression] = compiler.compile(str)
  def apply(expr: XPathExpression): Expression = new Expression(expr)
}