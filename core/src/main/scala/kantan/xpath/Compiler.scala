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

/** Compiles XPath expressions.
  *
  * There's very little reason to interact with this class directly, it's only meant to act as a bridge between
  * [[XPathCompiler]] and [[Query]].
  */
trait Compiler[A] {
  /** Turns the specified XPath expression into a valid [[Query]]. */
  def compile(str: String): CompileResult[Query[DecodeResult[A]]]
}

/** Provides implicit methods to summon [[Compiler]] instances. */
object Compiler {
  /** Type level trickery, this lets us makes the difference between types and type constructors. */
  type Id[A] = A

  /** Compiles XPath expressions into [[Query]] instances that will only retrieve the first match. */
  implicit def xpath1[A](implicit xpath: XPathCompiler, da: NodeDecoder[A]): Compiler[Id[A]] = new Compiler[Id[A]] {
    override def compile(str: String) = xpath.compile(str).map(expr ⇒ Query { n ⇒
      da.decode(Option(expr.evaluate(n, XPathConstants.NODE).asInstanceOf[Node]))
    })
  }

  /** Compiles XPath expressions into [[Query]] instances that retrieve very match. */
  implicit def xpathN[F[_], A]
  (implicit xpath: XPathCompiler, da: NodeDecoder[A], cbf: CanBuildFrom[Nothing, A, F[A]]): Compiler[F[A]] =
    new Compiler[F[A]] {
      def fold(i: Int, nodes: NodeList, out: mutable.Builder[A, F[A]]): DecodeResult[F[A]] = {
        if(i < nodes.getLength) {
          da.decode(Option(nodes.item(i))) match {
            case Result.Success(a) ⇒
              out += a
              fold(i + 1, nodes, out)
            case f@Result.Failure(_) ⇒ f
          }
        }
        else DecodeResult.success(out.result())

      }

      override def compile(str: String) =
        xpath.compile(str).map(expr ⇒ Query { n ⇒
          fold(0, expr.evaluate(n, XPathConstants.NODESET).asInstanceOf[NodeList], cbf())
        })
    }
}
