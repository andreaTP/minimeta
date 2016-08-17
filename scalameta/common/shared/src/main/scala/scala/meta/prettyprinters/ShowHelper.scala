package scala.meta
package prettyprinters

import scala.language.experimental.macros

object ShowHelper {

  def sequence[T](xs: T*): Show.Result = macro scala.meta.internal.prettyprinters.ShowMacros.seq

  def meta[T](data: Any, xs: T*): Show.Result = macro scala.meta.internal.prettyprinters.ShowMacros.meta
}
