package Hometask4

//dependencies {
//   implementation(kotlin("reflect"))

import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


fun <T : Any> createRandomInstance(klass: KClass<T>): T?  // Нужны некоторые модификаторы, они осознанно опущены, тут на подумать =)
{
    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    if(klass.primaryConstructor == null){
        return null
    }

    val constructorArray = Array<Any?>(klass.primaryConstructor!!.parameters.size){}
    println(constructorArray.size)

    //Int, Double, String, Boolean. Если параметр принадлежит другому типу, то вместо него принимаем null
    klass.primaryConstructor!!.parameters.forEach { i ->
        constructorArray[i.index] =
        (
            if (i.type.isMarkedNullable && Random.nextInt(0, 5) == 1) null
            else when (i.type.classifier) {
                Boolean::class -> Random.nextBoolean()
                Int::class -> Random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE)
                Double::class -> Random.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE)
                String::class -> getRandomString(Random.nextInt(5, 20))
                else -> null
            }
        )
    }

    println("[" + constructorArray.joinToString(separator = ", ") { it?.toString() ?: "null" } + "]")
    return klass.primaryConstructor!!.call(*constructorArray)
}




object MySingleton {
    var cogo_poslushat = "Шампур - Кровать цветов"
}

fun testProperties(){
    class MyClass(
        val lol: Boolean,
        var st: String?,
        var first: String
    )

    data class NERD(
        val lol: Boolean,
        var st: String?,
        var first: String,
        var double: Double = 1000.1,
        var int: Int = 10
    )

    class ZeroParamConstructor{
        var cogo_poslushat = "YAMEI - synesthesia!"
    }

    class NullConst


    println(createRandomInstance(MyClass::class))
    println(createRandomInstance(NERD::class))
    println(createRandomInstance(ZeroParamConstructor::class))
    println(createRandomInstance(NullConst::class))
    println(createRandomInstance(MySingleton::class))
}



fun main() {
    testProperties()
}

