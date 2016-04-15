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

import scala.collection.generic.CanBuildFrom

trait Expression[A] {
  def all[F[_]](n: Node)(implicit cbf: CanBuildFrom[Nothing, A, F[A]]): F[A]
  def first(n: Node): A

  def liftAll[F[_]](implicit cbf: CanBuildFrom[Nothing, A, F[A]]): Node ⇒ F[A] = (n: Node) ⇒ all(n)
  def liftFirst: Node ⇒ A = (n: Node) ⇒ first(n)

  def map[B](f: A ⇒ B): Expression[B]
}
