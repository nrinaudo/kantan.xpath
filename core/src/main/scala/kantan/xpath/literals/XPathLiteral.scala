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

package kantan.xpath.literals

import kantan.xpath.XPathCompiler
import kantan.xpath.XPathExpression

import scala.reflect.macros.blackbox.Context

final class XPathLiteral(val sc: StringContext) extends AnyVal {
  def xp(args: Any*): XPathExpression =
    macro XPathLiteral.xpImpl
}

// Relatively distatefull trick to get rid of spurious warnings.
trait XPathLiteralMacro {
  def xpImpl(c: Context)(args: c.Expr[Any]*): c.Expr[XPathExpression]
}

object XPathLiteral extends XPathLiteralMacro {
  override def xpImpl(c: Context)(args: c.Expr[Any]*): c.Expr[XPathExpression] = {
    import c.universe._

    c.prefix.tree match {
      case Apply(_, List(Apply(_, List(lit @ Literal(Constant(str: String)))))) =>
        implicitly[XPathCompiler].compile(str) match {
          case Left(_) => c.abort(c.enclosingPosition, s"Illegal XPath expression: '$str'")
          case Right(_) =>
            reify {
              val spliced = c.Expr[String](lit).splice

              implicitly[XPathCompiler]
                .compile(spliced)
                .getOrElse(sys.error(s"Illegal XPath expression: '$spliced'"))
            }
        }

      case _ =>
        c.abort(c.enclosingPosition, "xp can only be used on string literals")
    }
  }
}

trait ToXPathLiteral {
  implicit def toXPathLiteral(sc: StringContext): XPathLiteral =
    new XPathLiteral(sc)
}
