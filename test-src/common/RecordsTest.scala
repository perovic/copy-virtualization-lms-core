package scala.virtualization.lms
package common
import scala.lms.epfl._

import scala.lms.epfl.test2._
//import scala.lms.epfl.test3._

import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext
import org.scala_lang.virtualized.Struct
import scala.lms.common._

@virtualize
trait BasicProg extends TestOps {
  def f(s: Rep[String]): Rep[String] = {
    val res = Record(name = s, lastName = s)
    res.lastName
  }
}

@virtualize
trait NestedProg extends TestOps {
  def f(s: Rep[String]): Rep[String] = {
    val res = Record(name = s, lastName = Record(fathers = s, last = s))
    val x = res.lastName
    x.fathers
  }
}

@virtualize
trait AsArgumentsProg extends TestOps {
  //implicit def thisTyp[T<:Record:Manifest]: Typ[T] = recordTyp[{name:Rep[String]}]
  //implicit def lift[T: Manifest](t: T): Rep[T]= unit(t)
  def f(s: Rep[String]): Rep[String] = {
    val x = Record(name = s)
    val y = Record(name = s)
    (if (unit(true)) x else y).name
  }
}

@virtualize
trait MixedTypesProg extends TestOps {
  implicit def lift[T: Typ](t: T): Rep[T]= unit(t)
  def f(s: Rep[String]): Rep[String] =
    Record(name = s, lastName = "last").lastName
}

//@virtualize
//trait FlatMapProg extends TestOps with ListOps {
//  implicit def lift[T: Manifest](t: T): Rep[T]= unit(t)
//  type Names = List[Record{val name: String}]
//  def f(s: Rep[String]): Rep[String] = {
//    val xs: Rep[List[Int]] = List(1,2,3)
//    val ys: Rep[Names] = List(Record(name = "A"),Record(name = "B"))
//    val xs1 = xs.flatMap { b => ys }
//    "xx"
//  }
//}


trait TestOps extends Functions with Equal with IfThenElse with StructOps with BooleanOps with StringOps with RecordOps
trait TestExp extends TestOps with FunctionsExp with EqualExp with IfThenElseExp with StructExp with BooleanOpsExp with StringOpsExp //with RecordOpsExp
trait TestGen extends ScalaGenFunctions with ScalaGenEqual with ScalaGenIfThenElse with ScalaGenStruct {val IR: TestExp}

class TestRecords extends FileDiffSuite {

  val prefix = home + "test-out/common/records-"

  def testRecordsBasic = {
    withOutFile(prefix+"basic") {
      object BasicProgExp extends BasicProg with TestExp with PrimitiveOpsExp with ArrayOpsExp with SeqOpsExp
      import BasicProgExp._

      val p = new TestGen { val IR: BasicProgExp.type = BasicProgExp }
      val stream = new java.io.PrintWriter(System.out)
      p.emitSource(f, "RecordsBasic", stream)
      p.emitDataStructures(stream)
    }
    assertFileEqualsCheck(prefix+"basic")
  }

  def testRecordsNested = {
    withOutFile(prefix+"nested") {
      object NestedProgExp extends NestedProg with PrimitiveOpsExp with ArrayOpsExp with SeqOpsExp with TestExp
      import NestedProgExp._

      val p = new TestGen { val IR: NestedProgExp.type = NestedProgExp }
      val stream = new java.io.PrintWriter(System.out)
      p.emitSource(f, "RecordsNested", stream)
      p.emitDataStructures(stream)
    }
    assertFileEqualsCheck(prefix+"nested")
  }

  def testAsArguments = {
    withOutFile(prefix+"as-arguments") {
      object AsArgumentsProgExp extends AsArgumentsProg with TestExp with PrimitiveOpsExp with ArrayOpsExp with SeqOpsExp
      import AsArgumentsProgExp._

      val p = new TestGen { val IR: AsArgumentsProgExp.type = AsArgumentsProgExp }
      val stream = new java.io.PrintWriter(System.out)
      p.emitSource(f, "AsArguments", stream)
      p.emitDataStructures(stream)
    }
    assertFileEqualsCheck(prefix+"as-arguments")
  }

  def testMixedTypes = {
    withOutFile(prefix+"mixed-types") {
      object MixedTypesProgExp extends MixedTypesProg with TestExp with PrimitiveOpsExp with ArrayOpsExp with SeqOpsExp
      import MixedTypesProgExp._

      val p = new TestGen { val IR: MixedTypesProgExp.type = MixedTypesProgExp }
      val stream = new java.io.PrintWriter(System.out)
      p.emitSource(f, "MixedTypes", stream)
      p.emitDataStructures(stream)
    }
    assertFileEqualsCheck(prefix+"mixed-types")
  }
}
