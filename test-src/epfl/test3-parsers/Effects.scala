package scala.lms
package epfl
package test3

import common._
import test1._
import test2._


trait Effects extends Base {

  type State
  type Effectful[A]

  implicit def effectfulTyp[A:Typ]: Typ[Effectful[A]]

  def noEffect: State
  def bindEffect[A:Typ](x: State, y: Rep[A]): State
  def reifyState[A:Typ](x: Rep[A], y: State): Rep[Effectful[A]]

  var context: State = _

  def reflectEffect[A:Typ](x: Rep[A]): Rep[A] = {
    context = bindEffect(context, x)
    x
  }

  def reifyEffects[A:Typ](block: => Rep[A]): Rep[Effectful[A]] = {
    val save = context
    context = noEffect

    val result = block
    val resultR = reifyState(result, context)
    context = save
    resultR
  }


}

trait Control extends test3.Effects with BaseExp {

  implicit def effectfulTyp[A:Typ]: Typ[Effectful[A]] = {
    implicit val ManifestTyp(m) = typ[A]
    manifestTyp
  }

  case class OrElse[A:Typ](x: List[Rep[Effectful[A]]]) extends Def[A]

  //  def orElse[A](xs: List[Rep[Effectful[A]]]): Rep[A] = reflectEffect(OrElse(xs))
  def orElse[A:Typ](xs: List[Rep[Effectful[A]]]): Rep[A] = OrElse(xs)

  // OrElse will be pure if all branches contain only match effects!!
  // if any branch contains output, OrElse will be impure
  // (not yet implemented)

  // stuff below could be separated

  type State = List[Rep[Any]]
  abstract class Effectful[A]

  case class Reify[A:Typ](x: Rep[A], es: List[Rep[Any]]) extends Def[Effectful[A]]
  case class Pure[A:Typ](x: Rep[A]) extends Exp[Effectful[A]]

  def noEffect: State = Nil
  def bindEffect[A:Typ](x: State, y: Rep[A]): State = x:::List(y)
  def reifyState[A:Typ](x: Rep[A], y: State): Rep[Effectful[A]] = y match {
    case Nil => Pure(x)
    case _ => Reify(x, y)
  }
}
