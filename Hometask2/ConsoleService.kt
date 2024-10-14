package Hometask2

interface ConsoleServiceInterface {
    fun work()
}

object ConsoleService : ConsoleServiceInterface {
    private val figureService = FigureService


    override fun work() {
        while (true) {
            println()
            writeHelp()
            try {
                val operation = getOperation(readln())
                when (operation) {
                    Operation.INSERT -> addFigure()

                    Operation.GET_AREA -> {
                        println("Суммарная площадь всех фигур: ${figureService.getTotalArea()}")
                    }

                    Operation.GET_PERIMETER -> {
                        println("Суммарный периметр всех фигур: ${figureService.getTotalPerimeter()}")
                    }

                    Operation.HELP -> println("I cannot help you bro I am sorry")

                    Operation.EXIT -> {
                        println("Спасибо за то, что использовал мой сервис! <3")
                        break
                    }
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    //////////////////////////////OPERATIONS//////////////////////////////

    private fun writeHelp() {
        println("Введите тип операции, которую хотите исполнить ")
        val result = Operation.entries
        result.forEach {
            println("номер ${it.ordinal + 1}: ${it.name}")
        }
    }

    private fun addFigure() {
        try{
            println("Введите тип фигуры: 1 = Square, 2 = Circle")
            val figureType = readln()
            //я знаю что эта строка жестко привязана к реализации, а не к интерфейсу, но что поделать
            println("Введите значение property (радиус для Circle или сторону для Square)")
            val property = readln().toDouble()

            when (figureType) {
                "1", "Square"  -> figureService.addSquare(property)
                "2", "Circle" -> figureService.addCircle(property)
                else -> throw WrongOperationTypeException()
            }
        }
        catch (e: Exception){
            println(e.message)
        }

    }
}


