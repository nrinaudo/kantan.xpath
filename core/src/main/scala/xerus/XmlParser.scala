package xerus

import javax.xml.parsers.DocumentBuilderFactory

import org.xml.sax.InputSource

// TODO: error handling
trait XmlParser {
  def parse(source: InputSource): Document
}

object XmlParser {
  def apply(f: InputSource => Document): XmlParser = new XmlParser {
    override def parse(source: InputSource): Document = f(source)
  }

  implicit val builtIn: XmlParser = {
    val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
    XmlParser(source => factory.newDocumentBuilder().parse(source))
  }
}
