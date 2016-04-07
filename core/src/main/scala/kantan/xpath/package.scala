package kantan

import kantan.codecs.{Decoder, Result}

package object xpath {
  type NodeDecoder[A] = Decoder[Node, A, DecodeError, codecs.type]

  // - Result types ----------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  type ReadResult[A] = Result[ReadError, A]
  type DecodeResult[A] = Result[DecodeError, A]
  type ParseResult = Result[ParseError, Node]


  // - XML types -------------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  type Element = org.w3c.dom.Element
  type Node = org.w3c.dom.Node
  type Document = org.w3c.dom.Document
  type Attr = org.w3c.dom.Attr
  type NodeList = org.w3c.dom.NodeList
}
