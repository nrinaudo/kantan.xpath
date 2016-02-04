package grind

import javax.xml.parsers.DocumentBuilderFactory

import org.xml.sax.InputSource

import scala.util.Try

trait XmlParser {
  def parse(source: InputSource): Option[Document]
}

object XmlParser {
  def apply(f: InputSource => Option[Document]): XmlParser = new XmlParser {
    override def parse(source: InputSource) = f(source)
  }

  implicit val builtIn: XmlParser = {
    val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
    XmlParser(source => Try(factory.newDocumentBuilder().parse(source)).toOption)
  }
}
