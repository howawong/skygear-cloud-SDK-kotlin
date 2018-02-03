package io.skygear.cloud.demo

@Op("hello:world")
class Hello{
    fun handle(): String {
        return "{\"Hello\": \"World\"}"
    }
}
