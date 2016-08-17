package scala.meta.tests

object ScalaJsHelper {

  import scala.language.experimental.macros
  import scala.reflect.macros.blackbox.Context

  def getTreeSyntax: String = macro getTreeSyntaxImpl

  def getTreeSyntaxImpl(c: Context) = {
    import c.universe._
    c.Expr[String](q"""{
      val tree = dialects.Scala211("foo + bar // baz").parse[Term].get
      tree.syntax.toString
    }""")
  }
}
