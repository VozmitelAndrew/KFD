package Hometask2

interface FigureServiceInterface {
    fun addSquare(property: Double)
    fun addCircle(property: Double)
    fun getPerimeter(): Double
    fun getArea(): Double
}

class FigureService : FigureServiceInterface {
    private val figures: MutableList<Shape> = mutableListOf()

    override fun addSquare(property: Double) {
        figures.add(Square(property))
    }

    override fun addCircle(property: Double) {
        figures.add(Circle(property))
    }

    override fun getPerimeter(): Double {
        return figures.sumOf {
            it.getPerimeter()
        }
    }

    override fun getArea(): Double {
        return figures.sumOf {
            it.getCircumference()
        }
    }
}
