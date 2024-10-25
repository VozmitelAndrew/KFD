package Delegates

import kotlin.properties.Delegates

class User {
    var name: String by Delegates.observable("no name :( ") {
            prop, old, new ->
        println("${prop.name} changed: $old -> $new")
    }
}

fun main() {
    val user = User()
    user.name = "first :D "
    user.name = "second :( "
}




