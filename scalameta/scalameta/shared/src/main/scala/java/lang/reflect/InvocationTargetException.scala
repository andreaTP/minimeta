package java.lang.reflect

//This should go into Scala.Js
class InvocationTargetException(target: Throwable, s: String)
    extends ReflectiveOperationException(target) {

  def getTargetException(): Throwable = getCause()

}
