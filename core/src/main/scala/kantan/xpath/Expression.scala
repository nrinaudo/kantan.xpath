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

trait Expression[A] extends (Node ⇒ A) { self ⇒
  def apply(n: Node): A
  def map[B](f: A ⇒ B): Expression[B] = new Expression[B] {
    override def apply(n: Node) = f(self(n))
  }
}

object Expression {
  def apply[A](str: String)(implicit cmp: Compiler[A]): XPathResult[Expression[DecodeResult[A]]] = cmp.compile(str)
}
