package grind

import javax.xml.xpath.{XPathConstants, XPathExpression}

import scala.collection.generic.CanBuildFrom

case class Expression(expr: XPathExpression) {
  def first[A](n: Node)(implicit da: NodeDecoder[A]): DecodeResult[A] = {
    val res = expr.evaluate(n, XPathConstants.NODE).asInstanceOf[Node]
    if(res == null) DecodeResult.NotFound
    else            da.decode(res)
  }

  def unsafeFirst[A](n: Node)(implicit da: NodeDecoder[A]): A = first(n).get

  def liftFirst[A](implicit da: NodeDecoder[A]): Node => DecodeResult[A] = n => first(n)

  def liftUnsafeFirst[A](implicit da: NodeDecoder[A]): Node => A = n => unsafeFirst(n)

  def all[F[_], A](n: Node)(implicit da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, DecodeResult[A], F[DecodeResult[A]]]): F[DecodeResult[A]] = {
    val res = expr.evaluate(n, XPathConstants.NODESET).asInstanceOf[NodeList]
    val out = cbf()
    for(i <- 0 until res.getLength) out += da.decode(res.item(i))
    out.result()
  }

  def unsafeAll[F[_], A](n: Node)(implicit da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, A, F[A]]): F[A] = {
    val res = expr.evaluate(n, XPathConstants.NODESET).asInstanceOf[NodeList]
    val out = cbf()
    for(i <- 0 until res.getLength) out += da.decode(res.item(i)).get
    out.result()
  }

  def liftAll[F[_], A](implicit da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, DecodeResult[A], F[DecodeResult[A]]]): Node => F[DecodeResult[A]] =
    n => all(n)

  def liftUnsafeAll[F[_], A](implicit da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, A, F[A]]): Node => F[A] =
    n => unsafeAll(n)
}

object Expression {
  def apply(str: String)(implicit compiler: XPathCompiler): Option[Expression] =
    compiler.compile(str)
}