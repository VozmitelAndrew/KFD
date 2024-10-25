package Delegates

val lazyValue: String by lazy {
    println("computed!")
    //ждать 3 секунлы
    Thread.sleep(3000)
    "Hello"
}

fun main() {
    println("Some logic")
    println(lazyValue)
    println("Other logic")
}


