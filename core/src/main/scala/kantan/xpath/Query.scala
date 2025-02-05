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

/** Compiled XPath expression.
  *
  * Instance creation is achieved through the [[Query$ companion object]].
  */
trait Query[A] extends Serializable { self =>
  def eval(n: Node): A

  def map[B](f: A => B): Query[B] =
    Query(n => f(self.eval(n)))

  def flatMap[B](f: A => Query[B]): Query[B] =
    Query(n => f(self.eval(n)).eval(n))
}

/** Provides convenient methods for XPath expression compilation.
  *
  * Actual compilation is done through instances of [[Compiler]]. Methods declared here will simply summon the right
  * implicit value.
  */
object Query {
  def apply[A](f: Node => A): Query[A] =
    new Query[A] {
      override def eval(n: Node): A =
        f(n)
    }

  def apply[A: Compiler](expr: XPathExpression): Query[DecodeResult[A]] =
    Compiler[A].compile(expr)

  /** Compiles the specified XPath expression. */
  def compile[A: Compiler](str: String)(implicit xpath: XPathCompiler): CompileResult[Query[DecodeResult[A]]] =
    xpath.compile(str).map(Compiler[A].compile)

  /** Compiles the specified XPath expression. */
  def unsafeCompile[A: Compiler](str: String)(implicit xpath: XPathCompiler): Query[DecodeResult[A]] =
    Query.compile(str).getOrElse(sys.error(s"Not a valid XPath expression: '$str'."))
}
