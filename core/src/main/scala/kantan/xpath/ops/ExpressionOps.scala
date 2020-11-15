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

package kantan.xpath.ops

import kantan.xpath.{DecodeResult, Query}

final class ExpressionOps[A](val expr: Query[DecodeResult[A]]) extends AnyVal {
  def mapResult[B](f: A => B): Query[DecodeResult[B]] = expr.map(_.map(f))
}

trait ToExpressionOps {
  implicit def toXPathExpressionOps[A](expr: Query[DecodeResult[A]]): ExpressionOps[A] = new ExpressionOps(expr)
}

object expression extends ToExpressionOps
