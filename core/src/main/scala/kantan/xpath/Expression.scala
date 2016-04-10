/*
 * Copyright 2016 Nicolas Rinaudo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kantan.xpath

import javax.xml.xpath.{XPathConstants, XPathExpression}
import kantan.codecs.Result
import scala.annotation.tailrec
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable

class Expression private[xpath] (val expr: XPathExpression) {
  /** Finds the first node that matches the expression and evaluates it as an `A`. */
  def first[A](n: Node)(implicit da: NodeDecoder[A]): DecodeResult[A] = {
    val res = expr.evaluate(n, XPathConstants.NODE).asInstanceOf[Node]
    if(res == null) DecodeResult.NotFound
    else            da.decode(res)
  }

  /** Finds all nodes matching the expression and evaluate them as an `F[A]]`, where `F` is a collection class. */
  def every[F[_], A](n: Node)(implicit da: NodeDecoder[A],
                              cbf: CanBuildFrom[Nothing, DecodeResult[A], F[DecodeResult[A]]])
  : F[DecodeResult[A]] = {
    val res = expr.evaluate(n, XPathConstants.NODESET).asInstanceOf[NodeList]
    val out = cbf()
    for(i ← 0 until res.getLength) out += da.decode(res.item(i))
    out.result()
  }

  def all[F[_], A](n: Node)
                  (implicit da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, A, F[A]]): DecodeResult[F[A]] = {
    val res = expr.evaluate(n, XPathConstants.NODESET).asInstanceOf[NodeList]

    @tailrec
    def loop(i: Int, acc: mutable.Builder[A, F[A]]): DecodeResult[F[A]] =
      if(i >= res.getLength) Result.success(acc.result())
      else da.decode(res.item(i)) match {
        case Result.Success(a) ⇒
          acc += a
          loop(i + 1, acc)
        case f@Result.Failure(_) ⇒ f
      }

    loop(0, cbf())
  }

  def unsafe: UnsafeExpression = new UnsafeExpression(this)

  def liftFirst[A: NodeDecoder]: (Node ⇒ DecodeResult[A]) = n ⇒ first(n)
  def liftAll[F[_], A: NodeDecoder](implicit cbf: CanBuildFrom[Nothing, A, F[A]]): (Node ⇒ DecodeResult[F[A]]) =
    n ⇒ all(n)

  def liftEvery[F[_], A]
  (implicit da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, DecodeResult[A], F[DecodeResult[A]]])
  : (Node ⇒ F[DecodeResult[A]]) = n ⇒ every(n)
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
