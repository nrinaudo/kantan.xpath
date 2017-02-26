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

import contextual._
import kantan.xpath.{XPathCompiler, XPathExpression}

object XPathLiteral extends Interpolator {
  @SuppressWarnings(Array("org.wartremover.warts.TraversableOps"))
  def contextualize(interpolation: StaticInterpolation): Seq[ContextType] = {
    interpolation.parts.foreach {
      case lit@Literal(_, _) ⇒
        implicitly[XPathCompiler].compile(interpolation.literals.head).leftMap { e ⇒
          interpolation.error(lit, 0, e.getMessage)
        }
      case hole@Hole(_, _) ⇒
        interpolation.abort(hole, "substitution is not supported")
    }
    Nil
  }

  def evaluate(interpolation: RuntimeInterpolation): XPathExpression =
    implicitly[XPathCompiler].compile(interpolation.parts.mkString).get
}
