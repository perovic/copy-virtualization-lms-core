package scala.lms
package common

import scala.language.experimental.macros
//import scala.reflect.macros.Universe
//import scala.reflect.macros.blackbox.Context



import org.scala_lang.virtualized.virtualize


//this file is purely for testing purposes
@virtualize
object Scopes extends App with RepFactory { //just for runnign and testing
  println("begin")
  /**
   * given `def OptiML[R](b: => R) = new Scope[OptiML, OptiMLExp, R](b)`
   *
   * `OptiML { body }` is expanded to:
   *
   *  trait DSLprog$ extends OptiML {
   *    def apply = body
   *  }
   *  (new DSLprog$ with OptiMLExp): OptiML with OptiMLExp
   */

  import scala.reflect.runtime.universe._ //for showCode and showRaw

  trait OptiML {
    def i:Int
  }
  trait OptiMLExp extends OptiML {
    def i = 9
  }
  case class Result(s:String)

  trait OptiMLR

  object DslSnippet {
    def apply[R](block: => R) = new Scope[OptiML, OptiMLExp, R](block) //OptiMLExp[R]
  }

  def OptiML[R](b: => R) = new Scope[OptiML, OptiMLExp, R](b)

  def f(block:Int) = new Scope[OptiML, OptiMLExp, R](block)

  DslSnippet {println("Hello World!")}
  DslSnippet { 5+3}
  DslSnippet { new Result("heyhey")}

//  object SimpleVector {
//    def apply[R](b: => R) = new Scope[OptiML, OptiMLExp[R], R](b)
//  }

//  object DeliteSnippet {
//    def apply[A,B](b: => Unit) = new Scope[A,B,Unit](b)
//  }

  /* https://github.com /stanford-ppl/Forge/blob/master/src/core/ForgeOps.scala */
  abstract class DSLGroup
  abstract class DSLType extends DSLGroup
  case class C(a:Any) extends DSLType

  trait TpeScope
  trait TpeScopeRunner[R] extends TpeScope {
//    def apply: R
//    val result = apply
//    _tpeScopeBox = null // reset
  }
  //var _tpeScopeBox: Rep[DSLType] = null
  def withTpe(tpe: Rep[DSLType]) = {
   // _tpeScopeBox = tpe
    new ChainTpe(tpe)
  }
  class ChainTpe(tpe: Rep[DSLType]) {
    def apply[R](block: => R) = new Scope[TpeScope, TpeScopeRunner[R], R](block)
  }

  /* https://github.com/stanford-ppl/Forge/blob/master/src/dsls/optila/Vector.scala */
  def addVectorCommonOps(v: Rep[DSLType], T: Rep[DSLType]) {
    val VectorCommonOps = withTpe(v) //try to avoid the separate instantiation
    VectorCommonOps {
      println("2-step")
    }
    withTpe(v) {println("1-step")}
  }
  case class Rep[+T](t:T)
  addVectorCommonOps(Rep(C(1)), Rep(C("s")))
}
