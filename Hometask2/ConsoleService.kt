package Hometask2

interface ConsoleServiceInterface {
    fun work()
}

class ConsoleService : ConsoleServiceInterface {
    private val figureService = FigureService()


    override fun work() {
        while (true) {
            println()
            writeHelp()
            try {
                val operation = getOperation(readln())
                when (operation) {
                    Operation.INSERT -> addFigure()

                    Operation.GET_AREA -> {
                        println("Суммарная площадь всех фигур: ${figureService.getArea()}")
                    }

                    Operation.GET_PERIMETER -> {
                        println("Суммарный периметр всех фигур: ${figureService.getPerimeter()}")
                    }

                    Operation.HELP -> println("Are you stupid or smth?")

                    Operation.EXIT -> {
                        println("Thanks for using this application! <3")
                        break
                    }
                }
            } catch (e: BadPropertyException) {
                println("Введено неверное значение параметра property: ${e.property}")
            } catch (e: WrongOperationTypeException) {
                println("Введен неизвестный тип операции: ${e.operation}")
            }
        }
    }

    //////////////////////////////OPERATIONS//////////////////////////////

    fun writeHelp() {
        println("Введите тип операции, которую хотите исполнить ")
        val result = Operation.entries
        result.forEach {
            println("номер ${it.ordinal + 1}: ${it.name}")
        }
    }

    private fun addFigure() {
        println("Введите тип фигуры: 1 = Square, 2 = Circle")
        val figureType = readln().toIntOrNull()
        //я знаю что эта строка жестко привязана к реализации, а не к интерфейсу, но что поделать
        println("Введите значение property (радиус для Circle или сторону для Square)")
        val property = readln().toDoubleOrNull()

        if (property == null || property <= 0) {
            throw BadPropertyException(property)
        }

        when (figureType) {
            1 -> figureService.addSquare(property)
            2 -> figureService.addCircle(property)
            else -> throw WrongOperationTypeException(figureType.toString())
        }
    }
}

class WrongOperationTypeException(val operation: String) : Exception()

