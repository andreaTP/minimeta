package scala.meta
package internal
package utils

import scala.collection.mutable

class WeakHashMap[K <: AnyRef, T] extends mutable.WeakHashMap[K, T] {}

object  WeakHashMap {

  def apply[K <: AnyRef, T]() = new utils.WeakHashMap[K, T]()

}
