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

import java.time._
import kantan.codecs.export.Exported
import kantan.codecs.strings.StringDecoder
import kantan.codecs.strings.java8.TimeDecoderCompanion

package object java8 extends TimeDecoderCompanion[Option[Node], DecodeError, codecs.type] {
  override def decoderFrom[D](d: StringDecoder[D]) = codecs.fromString(d)


  implicit val defaultInstantNodeDecoder: Exported[NodeDecoder[Instant]] =
    Exported(defaultInstantDecoder)
  implicit val defaultZonedDateTimeNodeDecoder: Exported[NodeDecoder[ZonedDateTime]] =
    Exported(defaultZonedDateTimeDecoder)
  implicit val defaultOffsetDateTimeNodeDecoder: Exported[NodeDecoder[OffsetDateTime]] =
    Exported(defaultOffsetDateTimeDecoder)
  implicit val defaultLocalDateTimeNodeDecoder: Exported[NodeDecoder[LocalDateTime]] =
    Exported(defaultLocalDateTimeDecoder)
  implicit val defaultLocalDateNodeDecoder: Exported[NodeDecoder[LocalDate]] =
    Exported(defaultLocalDateDecoder)
  implicit val defaultLocalTimeNodeDecoder: Exported[NodeDecoder[LocalTime]] =
    Exported(defaultLocalTimeDecoder)
}

