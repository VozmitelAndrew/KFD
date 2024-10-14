package Hometask2

class BadPropertyException(property: Double) : IllegalArgumentException("Невалидное значение: $property")
class WrongOperationTypeException() : Exception("Что-то пошло не так. Попробуйте снова")
