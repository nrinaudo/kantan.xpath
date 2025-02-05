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

import kantan.codecs.Decoder
import kantan.codecs.export.Exported
import kantan.codecs.strings.StringDecoder
import kantan.codecs.strings.java8.TimeDecoderCompanion
import kantan.codecs.strings.java8.ToFormatLiteral

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime

/** Declares [[kantan.xpath.NodeDecoder]] instances java8 date and time types.
  *
  * Note that the type for default codecs might come as a surprise: the wrapping `Exported` is used to lower their
  * priority. This is necessary because the standard use case will be to `import kantan.xpath.java8._`, which brings
  * both the instance creation and default instances in scope. Without this type trickery, custom instances and default
  * ones would always clash.
  */
package object java8
// I'm not entirely sure why an explicit org.w3c.dom.Node type was needed, but when upgrading from 2.13.6 to 2.13.7,
// kantan.xpath.Node fails to resolve for whatever reason...
    extends TimeDecoderCompanion[Option[org.w3c.dom.Node], DecodeError, codecs.type] with ToFormatLiteral {

  override def decoderFrom[D](d: StringDecoder[D]): Decoder[Option[Node], D, DecodeError, codecs.type] =
    codecs.fromString(d)

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
