package sbt
package std

	import complete.{DefaultParsers, Parsers}


/*object UseTask
{
		import Def._

	val set = setting { 23 }
	val plain = PlainTaskMacro task { 19 }

	val x = task { set.value }
	val y = task { true }
	val z = task { if(y.value) x.value else plain.value }
	val a = taskDyn { 
		if(y.value) z else x
	}
}*/
object Assign
{
	import java.io.File
	import Def.{Initialize,macroValueT,parserToInput}
//	import UseTask.{x,y,z,a,set,plain}

	val ak = TaskKey[Int]("a")
	val bk = TaskKey[Seq[Int]]("b")
	val ck = SettingKey[File]("c")
	val sk = TaskKey[Set[_]]("s")

	val ik = InputKey[Int]("i")
	val isk = InputKey[String]("is")
	val mk = SettingKey[Int]("m")
	val tk = TaskKey[Int]("t")
	val name = SettingKey[String]("name")
	val dummyt = TaskKey[complete.Parser[String]]("dummyt")
	val dummys = SettingKey[complete.Parser[String]]("dummys")
	val dummy3 = SettingKey[complete.Parser[(String,Int)]]("dummy3")
	val tsk: complete.Parser[TaskKey[String]] = ???

/*	def azy = sk.value

	def azy2 = appmacro.Debug.checkWild(Def.task{ sk.value.size })

	val settings = Seq(
		ak += z.value + (if(y.value) set.value else plain.value),
		ck := new File(ck.value, "asdf"),
		ak := sk.value.size,
		bk ++= Seq(z.value)
	)*/

		import DefaultParsers._
	val p = Def.setting { name.value ~> Space ~> ID }
	val is = Seq(
		mk := 3,
		name := "asdf",
		tk := (math.random*1000).toInt,
		isk := tsk.parsed.value, // ParserInput.wrap[TaskKey[String]](tsk).value 
//		isk := dummys.value.parsed , // should not compile: cannot use a task to define the parser
		ik := { if( tsk.parsed.value == "blue") tk.value else mk.value }
	)

	val it1 = Def.inputTask {
		tsk.parsed //"as" //dummy.value.parsed
	}
	val it2 = Def.inputTask {
		"lit"
	}
	// should not compile because getting the value from a parser involves getting the value from a task
	val it3: Initialize[InputTask[String]] = Def.inputTask[String] {
		tsk.parsed.value
	}
/*	// should not compile: cannot use a task to define the parser
	val it4 = Def.inputTask {
		dummyt.value.parsed
	}*/
	// should compile: can use a setting to define the parser
	val it5 = Def.inputTask {
		dummys.parsed
	}
	val it6 = Def.inputTaskDyn {
		val (x,i) = dummy3.parsed
		Def.task { tk.value + i}
	}


/*	def bool: Initialize[Boolean] = Def.setting { true }
	def enabledOnly[T](key: Initialize[T]): Initialize[Seq[T]] = Def.setting {
		val keys: Seq[T] = forallIn(key).value
		val enabled: Seq[Boolean] = forallIn(bool).value
		(keys zip enabled) collect { case (a, true) => a }
	}
	def forallIn[T](key: Initialize[T]): Initialize[Seq[T]] = Def.setting {
		key.value :: Nil
	}*/
}