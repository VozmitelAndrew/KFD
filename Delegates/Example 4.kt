package Delegates

class MutableUser(private val map: MutableMap<String, Any?>) {
    var name: String by map
    var age: Int     by map
}

fun main() {
    // Создаем MutableMap с начальными значениями
    val userData = mutableMapOf<String, Any?>(
        "name" to "Alice",
        "age" to 30
    )

    // Создаем экземпляр MutableUser
    val user = MutableUser(userData)

    // Получаем значения полей
    println("Name: ${user.name}")
    println("Age: ${user.age}")

    // Изменяем значения через делегирование
    user.name = "Bob"
    user.age = 35

    // Выводим обновленные значения
    println("Updated Name: ${user.name}")
    println("Updated Age: ${user.age}")
}


