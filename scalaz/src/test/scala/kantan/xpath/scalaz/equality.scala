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

package kantan.xpath.scalaz

import kantan.xpath.Query
import kantan.xpath.XmlSource
import kantan.xpath.scalaz.arbitrary._
import org.scalacheck.Arbitrary
import scalaz.Equal
import scalaz.Scalaz._

object equality extends kantan.codecs.scalaz.laws.discipline.EqualInstances {

  implicit def xmlSourceEqual[A: Arbitrary]: Equal[XmlSource[A]] =
    new Equal[XmlSource[A]] {

      override def equal(a1: XmlSource[A], a2: XmlSource[A]) =
        kantan.codecs.laws.discipline.equality.eq(a1.asNode, a2.asNode) { (d1, d2) =>
          d1 === d2
        }
    }

  implicit def queryEqual[A: Equal: Arbitrary]: Equal[Query[A]] =
    new Equal[Query[A]] {
      implicit val arb = arbNode((a: A) => a.toString)
      override def equal(a1: Query[A], a2: Query[A]) =
        kantan.codecs.laws.discipline.equality.eq(a1.eval, a2.eval) { (d1, d2) =>
          d1 === d2
        }
    }

}
