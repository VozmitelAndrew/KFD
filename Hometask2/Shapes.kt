package Hometask2

sealed class Shape(var property: Double) {
    init {
        if (property <= 0) {
            throw BadPropertyException(property)
        }
    }
}

data class Square(val side: Double) : Shape(side)
data class Circle(val radius: Double) : Shape(radius)


