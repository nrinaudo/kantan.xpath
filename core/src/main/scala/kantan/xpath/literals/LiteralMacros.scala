/*
 * Copyright 2017 Nicolas Rinaudo
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

package kantan.xpath.literals

import javax.xml.xpath.XPathFactory
import kantan.xpath.{XPathCompiler, XPathExpression}
import kantan.xpath.macros._

object LiteralMacros {
  private val compiler = XPathFactory.newInstance().newXPath()

  def xpImpl(c: Context)(args: c.Expr[Any]*): c.Expr[XPathExpression] = {
    import c.universe._

    c.prefix.tree match {
      case Apply(_, List(Apply(_, List(lit@Literal(Constant(xpath: String)))))) ⇒
        try {
          compiler.compile(xpath)
          reify(implicitly[XPathCompiler].compile(c.Expr[String](lit).splice).get)
        }
        catch {
          case e: Exception ⇒ c.abort(c.enclosingPosition, s"Illegal xpath expression: $xpath")
        }

      case _ ⇒
        c.abort(c.enclosingPosition, "xp can only be used on string literals")
    }
  }
}
