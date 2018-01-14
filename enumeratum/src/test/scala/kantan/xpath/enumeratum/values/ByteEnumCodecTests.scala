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
package enumeratum.values

import enumeratum.arbitrary._
import kantan.codecs.enumeratum.laws.discipline.EnumeratedByte
import laws.discipline._

class ByteEnumCodecTests extends DisciplineSuite {

  checkAll("NodeDecoder[EnumeratedByte]", SerializableTests[NodeDecoder[EnumeratedByte]].serializable)
  checkAll("NodeDecoder[EnumeratedByte]", NodeDecoderTests[EnumeratedByte].decoder[String, Float])

}
