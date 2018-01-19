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
package literals

import contextual._

object XPathLiteral extends Interpolator {
  type Output = XPathExpression

  @SuppressWarnings(
    Array("org.wartremover.warts.TraversableOps", "org.wartremover.warts.Product", "org.wartremover.warts.Serializable")
  )
  def contextualize(interpolation: StaticInterpolation): Seq[ContextType] = {
    interpolation.parts.foreach {
      case lit @ Literal(_, _) ⇒
        implicitly[XPathCompiler].compile(interpolation.literals.head).left.map { e ⇒
          interpolation.error(lit, 0, e.getMessage)
        }
      case hole @ Hole(_, _) ⇒
        interpolation.abort(hole, "substitution is not supported")
    }
    Nil
  }

  @SuppressWarnings(Array("org.wartremover.warts.EitherProjectionPartial"))
  def evaluate(interpolation: RuntimeInterpolation): XPathExpression =
    implicitly[XPathCompiler].compile(interpolation.parts.mkString).right.get
}
