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

import org.apache.xerces.parsers.DOMParser
import org.cyberneko.html.HTMLConfiguration

package object nekohtml {
  implicit val defaultParser: XmlParser = XmlParser { s ⇒
    // Sane default configuration
    val conf = new HTMLConfiguration
    conf.setProperty("http://cyberneko.org/html/properties/names/elems", "lower")

    val parser = new DOMParser(conf)
    ParseResult {
      parser.parse(s)
      parser.getDocument
    }
  }
}
