package interop.grind

import org.apache.xerces.parsers.DOMParser
import org.cyberneko.html.HTMLConfiguration
import grind.XmlParser

package object nekohtml {
  implicit val defaultParser: XmlParser = XmlParser { s =>
    val conf = new HTMLConfiguration

    // Sane default configuration
    conf.setProperty("http://cyberneko.org/html/properties/names/elems", "lower")

    val parser = new DOMParser(conf)
    parser.parse(s)
    parser.getDocument
  }
}
