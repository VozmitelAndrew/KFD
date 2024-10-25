package Delegates

import kotlin.reflect.KProperty

class Delegate {
    private var remember = "HI!"

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!, $remember"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        remember = value
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }
}

class Example {
    var p: String by Delegate()
}

fun main(){
    val e = Example()
    println(e.p)
    e.p = "NEW!"
    println(e.p)
}

