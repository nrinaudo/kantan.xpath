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

import javax.xml.xpath.XPathConstants
import kantan.codecs.Result
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable

trait Compiler[A] {
  def compile(str: String): CompileResult[Expression[DecodeResult[A]]]
}

object Compiler {
  type Id[A] = A

  implicit def xpath1[A](implicit xpath: XPathCompiler, da: NodeDecoder[A]): Compiler[Id[A]] = new Compiler[Id[A]] {
    override def compile(str: String) =
      xpath.compile(str).map(expr ⇒ new Expression[DecodeResult[A]] {
        override def apply(n: Node) = {
          val res = expr.evaluate(n, XPathConstants.NODE).asInstanceOf[Node]
          if(res == null) DecodeResult.NotFound
          else            da.decode(res)
        }
      })
  }

  implicit def xpathN[F[_], A]
  (implicit xpath: XPathCompiler, da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, A, F[A]]): Compiler[F[A]] =
    new Compiler[F[A]] {
      def fold(i: Int, nodes: NodeList, out: mutable.Builder[A, F[A]]): DecodeResult[F[A]] = {
        if(i < nodes.getLength) {
          da.decode(nodes.item(i)) match {
            case Result.Success(a) ⇒
              out += a
              fold(i + 1, nodes, out)
            case f@Result.Failure(_) ⇒ f
          }
        }
        else DecodeResult.success(out.result())

      }

      override def compile(str: String) =
        xpath.compile(str).map(expr ⇒ new Expression[DecodeResult[F[A]]] {
          override def apply(n: Node) = fold(0, expr.evaluate(n, XPathConstants.NODESET).asInstanceOf[NodeList], cbf())
        })
    }
}
