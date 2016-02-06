package kantan.xpath

import javax.xml.xpath.{XPathConstants, XPathExpression}

import scala.annotation.tailrec
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable

class Expression private[xpath] (val expr: XPathExpression) {
  /** Finds the first node that matches the expression and evaluates it as an `A`. */
  def first[A: NodeDecoder](n: Node): DecodeResult[A] = Expression.first(n, expr)

  /** Finds all nodes matching the expression and evaluate them as an `F[A]]`, where `F` is a collection class. */
  def all[F[_], A](n: Node)(implicit da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, DecodeResult[A], F[DecodeResult[A]]]): F[DecodeResult[A]] =
    Expression.all(n, expr)(da.decode)

  def every[F[_], A](n: Node)(implicit da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, A, F[A]]): DecodeResult[F[A]] = {
    val res = expr.evaluate(n, XPathConstants.NODESET).asInstanceOf[NodeList]

    @tailrec
    def loop(i: Int, acc: mutable.Builder[A, F[A]]): DecodeResult[F[A]] =
      if(i >= res.getLength) DecodeResult.success(acc.result())
      else da.decode(res.item(i)) match {
        case DecodeResult.Success(a) =>
          acc += a
          loop(i + 1, acc)
        case _ => DecodeResult.Failure
      }

    loop(0, cbf())
  }

  /** Turns this expression into an unsafe one
    *
    * An unsafe expression is one that throws exception when error occurs, as opposed to wrapping them in a
    * [[DecodeResult.Failure]].
    */
  def unsafe: UnsafeExpression = new UnsafeExpression(expr)

  def liftFirst[A: NodeDecoder]: (Node => DecodeResult[A]) = n => first(n)
  def liftAll[F[_], A: NodeDecoder](implicit cbf: CanBuildFrom[Nothing, DecodeResult[A], F[DecodeResult[A]]]): (Node => F[DecodeResult[A]]) =
    n => all(n)
}

class UnsafeExpression private[xpath] (val expr: XPathExpression) {
  def first[A: NodeDecoder](n: Node): A = Expression.first(n, expr).get
  def all[F[_], A](n: Node)(implicit da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, A, F[A]]): F[A] =
    Expression.all(n, expr)(n => da.decode(n).get)
  def liftFirst[A: NodeDecoder]: (Node => A) = n => first(n)
  def liftAll[F[_], A: NodeDecoder](implicit cbf: CanBuildFrom[Nothing, A, F[A]]): (Node => F[A]) = n => all(n)
  def safe: Expression = new Expression(expr)
}

object Expression {
  def apply(str: String)(implicit compiler: XPathCompiler): Option[Expression] = compiler.compile(str)
  def apply(expr: XPathExpression): Expression = new Expression(expr)

  private[xpath] def first[A](n: Node, expr: XPathExpression)(implicit da: NodeDecoder[A]): DecodeResult[A] = {
    val res = expr.evaluate(n, XPathConstants.NODE).asInstanceOf[Node]
    if(res == null) DecodeResult.NotFound
    else            da.decode(res)
  }

  private[xpath] def all[A, F[_]](n: Node, expr: XPathExpression)(f: Node => A)(implicit cbf: CanBuildFrom[Nothing, A, F[A]]): F[A] = {
    val res = expr.evaluate(n, XPathConstants.NODESET).asInstanceOf[NodeList]
    val out = cbf()
    for(i <- 0 until res.getLength) out += f(res.item(i))
    out.result()
  }
}