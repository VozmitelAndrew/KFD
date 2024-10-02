package Hometask2

sealed class Shape(var property: Double, private var perimeter: Double, private var circumference: Double) {
    fun getPerimeter(): Double {
        return perimeter
    }

    fun getCircumference(): Double {
        return circumference
    }

    init {
        if (property <= 0) {
            throw BadPropertyException(property)
        }
    }
}

class Square(property: Double) : Shape(property, 4 * property, property * property) {
    override fun toString(): String = "Square(property=$property)"

    init{
        println(this.toString())
    }
}

class Circle(property: Double) : Shape(property, 2 * 3.14 * property, 3.14 * property * property) {
    override fun toString(): String = "Circle(property=$property)"

    init{
        println(this.toString())
    }
}

class BadPropertyException(val property: Double?) : Exception()
