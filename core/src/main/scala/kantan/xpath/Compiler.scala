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

import javax.xml.xpath.{XPath, XPathConstants, XPathExpression, XPathFactory}
import scala.collection.generic.CanBuildFrom
import scala.util.Try

trait  Compiler {
  // TODO: use something else than Option here. We might want a specialised Result.
  def compile[A: NodeDecoder](str: String): Option[Expression[DecodeResult[A]]]
}

object Compiler {
  private case class XPathImpl[A](expr: XPathExpression, decode: Node ⇒ A) extends Expression[A] {
    override def all[F[_]](n: Node)(implicit cbf: CanBuildFrom[Nothing, A, F[A]]) = {
      val res = expr.evaluate(n, XPathConstants.NODESET).asInstanceOf[NodeList]
      val out = cbf()
      for(i ← 0 until res.getLength) out += decode(res.item(i))
      out.result()
    }

    override def first(n: Node) = decode(expr.evaluate(n, XPathConstants.NODE).asInstanceOf[Node])

    override def map[B](f: A ⇒ B) = copy(decode = decode andThen f)
  }

  implicit val defaultXPath = XPathFactory.newInstance().newXPath()

  implicit def xpath(implicit factory: XPath): Compiler = new Compiler {
    override def compile[A](str: String)(implicit da: NodeDecoder[A]) =
      Try(XPathImpl(factory.compile(str), da.decode)).toOption
  }
}
