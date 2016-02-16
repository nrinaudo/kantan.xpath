package kantan.xpath

import org.apache.xerces.parsers.DOMParser
import org.cyberneko.html.HTMLConfiguration

package object nekohtml {
  implicit val defaultParser: XmlParser = XmlParser { s ⇒
    // Sane default configuration
    val conf = new HTMLConfiguration
    conf.setProperty("http://cyberneko.org/html/properties/names/elems", "lower")

    val parser = new DOMParser(conf)
    LoadingResult {
      parser.parse(s)
      parser.getDocument
    }
  }
}
