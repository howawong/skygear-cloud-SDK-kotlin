//DEPS com.sparkjava:spark-core:2.5.4
//DEPS org.slf4j:slf4j-simple:1.7.13
//DEPS org.reflections:reflections:0.9.11
//DEPS com.google.code.gson:gson:2.3.1
//INCLUDE op.kt
//INCLUDE app.kt

import spark.Spark.*
import org.reflections.Reflections
import com.google.gson.Gson

fun main(args: Array<String>) {
    val reflections = Reflections("io.skygear.cloud.demo")
    val ops = reflections.getTypesAnnotatedWith(Op::class.java)
    val opMap = HashMap<String, Class<*>>()
    ops.forEach{ s ->
        val name = s.getAnnotation(Op::class.java).name
        println(name)
        println(s)
        opMap.put(name, s)
    }
    port(9000)
    post("/") { req, res ->
      println(req.body())
      val gson = Gson()
      var map = HashMap<String, Object>()
      map = gson.fromJson(req.body(), map::class.java) 
      res.type("application/json") 
      val kind = map["kind"] as String?
      println(kind)
      if (kind == "init") {
          val op = "[" + opMap.keys.joinToString(separator=",", transform= {"{\"name\":\"" + it + "\"}"}) + "]"
          println(op)
          "{\"result\": {\"op\": " + op + ", \"handler\":[], \"hook\": [], \"timer\": [], \"provider\": [], \"event\": []}}"      
      }
      else
      if (kind == "op") {
        val name = map["name"] as String?
        val obj = opMap[name]?.newInstance() as Hello?
        println(obj)
        val result = obj?.handle() ?: "{}"
        "{\"result\": " + result +"}"
      }
      else {
        "{\"result\": {}}"
      }
    }
    notFound { req, res ->
      println(req.uri())
      println("not found")
      res.type("application/json") 
      "{\"message\": \"not found\"}"
    }
}
