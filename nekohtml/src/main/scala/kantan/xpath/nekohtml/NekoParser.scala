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

package kantan.xpath.nekohtml

import kantan.xpath.InputSource
import kantan.xpath.Node
import kantan.xpath.ParseResult
import kantan.xpath.XmlParser
import org.apache.xerces.parsers.DOMParser
import org.cyberneko.html.HTMLConfiguration

class NekoParser(val conf: HTMLConfiguration) extends XmlParser {
  override def parse(source: InputSource): ParseResult[Node] =
    conf.synchronized {
      val parser = new DOMParser(conf)

      ParseResult {
        parser.parse(source)
        parser.getDocument
      }
    }
}
