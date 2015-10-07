package scala.lms
package common

import scala.language.experimental.macros
import scala.reflect.macros.internal.macroImpl
import scala.reflect.macros.blackbox._
import scala.reflect.macros._

/**
 * Created by cedricbastin on 07/10/15.
 */
object ScopeMacro {
  def impl[Interface: c.WeakTypeTag, Implementation: c.WeakTypeTag, Result: c.WeakTypeTag]
  (c: scala.reflect.macros.blackbox.Context)
  (body: c.Tree): c.Expr[Unit]= { //c.Expr[ => Result]
    import c.universe._
    //generate trait and instantiation?
    reify(println("macro works"))//.tree
  }

  object Main extends App {
    println("end")
  }
}

