package Hometask2

interface FigureServiceInterface {
    fun addSquare(property: Double)
    fun addCircle(property: Double)
    fun getTotalPerimeter(): Double
    fun getTotalArea(): Double
}

object FigureService : FigureServiceInterface {
    override fun addSquare(property: Double) {
        shapes.add(Square(property))
    }

    override fun addCircle(property: Double) {
        shapes.add(Circle(property))
    }

    override fun getTotalPerimeter(): Double {
        return shapes.sumOf {
            when (it) {
                is Square -> 4 * it.side
                is Circle -> 2 * Math.PI * it.radius
            }
        }
    }

    override fun getTotalArea(): Double {
        return shapes.sumOf {
            when (it) {
                is Square -> it.side * it.side
                is Circle -> Math.PI * it.radius * it.radius
            }
        }
    }

    private val shapes: MutableList<Shape> = mutableListOf()
}