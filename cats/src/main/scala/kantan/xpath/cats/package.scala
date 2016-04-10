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

import _root_.cats.Eq
import _root_.cats.functor.Contravariant
import kantan.codecs.cats.CatsInstances

package object cats extends CatsInstances {
  /** `Eq` instance for errors. */
  implicit val readErrorEq = new Eq[ReadError] {
    override def eqv(x: ReadError, y: ReadError) = x == y
  }

  implicit val decodeErrorEq = new Eq[DecodeError] {
    override def eqv(x: DecodeError, y: DecodeError) = x == y
  }

  implicit val parseErrorEq = new Eq[ParseError] {
    override def eqv(x: ParseError, y: ParseError) = x == y
  }

  /** `Contravariant` instance for `XmlSource`. */
  implicit val xmlSource = new Contravariant[XmlSource] {
    override def contramap[A, B](fa: XmlSource[A])(f: B ⇒ A) = fa.contramap(f)
  }
}
