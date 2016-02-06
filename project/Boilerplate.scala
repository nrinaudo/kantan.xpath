import sbt._
import java.io.File

object Boilerplate {
  def decoder(arity: Int, out: StringBuilder): Unit = {
    out.append(s"  def decoder$arity")
    out.append((1 to arity).map(i => s"I$i").mkString("[", ", ", ", "))
    out.append("O](f: ")
    out.append((1 to arity).map(i => s"I$i").mkString("(", ", ", ")"))
    out.append(" => O)")
    out.append((1 to arity).map(i => s"x$i: Expression").mkString("(", ", ", ")"))
    out.append((1 to arity).map(i => s"e$i: Evaluator[I$i]").mkString("(implicit ", ", ", ")"))
    out.append(": NodeDecoder[O] = NodeDecoder { n =>\n")
    out.append("    for {\n")
    (1 to arity).foreach { i => out.append(s"      i$i <- e$i.evaluate(x$i, n)\n") }
    out.append("    } yield f")
    out.append((1 to arity).map(i => s"i$i").mkString("(", ",", ")\n"))
    out.append("  }\n")
    ()
  }

  def tuple(arity: Int, out: StringBuilder): Unit = {
    out.append(s"  def tuple$arity")
    out.append((1 to arity).map(i => s"I$i: Evaluator").mkString("[", ", ", "]"))
    out.append((1 to arity).map(i => s"x$i: Expression").mkString("(", ", ", ")"))
    out.append(": NodeDecoder")
    out.append((1 to arity).map(i => s"I$i").mkString("[(", ", ", ")] =\n"))
    out.append(s"    NodeDecoder.decoder$arity(")
    out.append((1 to arity).map(i => s"i$i: I$i").mkString("(", ", ", ")"))
    out.append(" => ")
    out.append((1 to arity).map(i => s"i$i").mkString("(", ", ", ")"))
    out.append(")")
    out.append((1 to arity).map(i => s"x$i").mkString("(", ", ", ")\n"))
    ()
  }

  def buildTrait(name: String)(f: (Int, StringBuilder) => Unit): String = {
    val out = new StringBuilder()
    out.append("package kantan.xpath\n")
    out.append(s"trait $name {\n")
    (1 to 22).foreach(i => f(i, out))
    out.append("}\n")
    out.result()
  }

  val traits: List[(String, (Int, StringBuilder) => Unit)] = List("TupleDecoders" -> tuple, "Decoders" -> decoder)

  def gen(dir: File): Seq[File] = {
    new File(dir, "kantan/xpath").mkdir()
    traits.map {
      case (name, f) =>
        val file = new File(dir, s"kantan/xpath/$name.scala")
        IO.write(file, buildTrait(name)(f), java.nio.charset.Charset.forName("UTF-8"))
        file
    }
  }
}
