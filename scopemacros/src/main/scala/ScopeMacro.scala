package scala.lms
package common

import scala.language.experimental.macros
import scala.reflect.macros.internal.macroImpl
import scala.reflect.macros.blackbox.Context
import scala.reflect.macros.Universe
import scala.collection.mutable.{ListBuffer, Stack}

/**
 * given `def OptiML[R](b: => R) = new Scope[OptiML, OptiMLExp, R](b)`
 *
 * `OptiML { body }` is expanded to:
 *
 *  trait DSLprog$ extends OptiML {
 *    def apply = body
 *  }
 *  (new DSLprog$ with OptiMLExp): OptiML with OptiMLExp
 *
 *
 */

object ScopeMacro {

  def simpl(c: Context)(body: c.Tree): c.Expr[Unit] = {
    import c.universe._
    reify(println("macro first"))
    reify(println("macro works"))
  }

//  def simplit[MyType: c.WeakTypeTag](c: Context)(arg: c.Expr[MyType]): c.Expr[MyType] = {
//    import c.universe._
//    //c.Expr(Apply(Select(q"9.toString", TermName("$plus"), q"10.toString")))
//    //c.TypeTag(weakTypeTag(MyType))
//
//    //reify(println("macro first"))
//    //reify(println("macro works"))
//  }

  def impl[Interface, Implementation, Result: c.WeakTypeTag]
  (c: Context)
  (body: c.Tree) = { //c.Expr[ => Result]
    val name = c.freshName("DSLprog")
    import c.universe._
    //generate trait and instantiation?
    //qprintln("macro works")""".splice
    //reify(println("macro works"))//creates expression
    //reify(q"trait DSLprog extends $weakTypeTag[Result]")
    //reify(print(c.Expr[Any](ref).splice)).tree)
    //c.Expr[Unit](Block(stats.toList, Literal(Constant(()))))
    //Expr(Block(List(ClassDef(Modifiers(ABSTRACT | INTERFACE | DEFAULTPARAM/TRAIT), TypeName("Flower"), List(), Template(List(Ident(TypeName("AnyRef"))), noSelfType, List()))), Literal(Constant(()))))
    c.Expr(Block(List(ValDef(Modifiers(), TermName("x"), TypeTree(), Literal(Constant(6)))), Literal(Constant(()))))
//    val evals = ListBuffer[ValDef]()
//    val stats = evals ++ refs.map(ref => reify(print(c.Expr[Any](ref).splice)).tree)
//    c.Expr[Unit](Block(stats.toList, Literal(Constant(()))))
  }

  object Main extends App {
    println("end")
  }
}

