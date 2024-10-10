package Hometask3

data class Response(
    val code: Int,
    val body: String?,
)

class ResponseActions(
    val code: Int,
    val body: String?,
){
    fun andDo(lambda: (Response) -> Unit) : Response {
        lambda(Response(code, body))
        return Response(code, body)
    }

    fun andExpect(block: ResponseActions.() -> Unit) : ResponseActions {
        this.block()
        return this
    }

    fun status(block: StatusFunc.() -> Unit): ResponseActions {
        StatusFunc(code).block()
        return this
    }

    fun body(block: BodyFunc.() -> Unit): ResponseActions {
        BodyFunc(body).block()
        return this
    }

    class StatusFunc(private val code: Int) {
        fun isOk() {
            if (code != 200)
                throw Exception("Status is not OK")
        }

        fun printStatus() {
            println(code)
        }

        fun isBadRequest() {
            if (code != 400) {
                throw Exception("Status is not Bad Request")
            }
        }

        fun isInternalServerError() {
            if (code != 500) {
                throw Exception("Status is not Internal Server Error")
            }
        }
    }


    class BodyFunc(private val body: String?) {
        fun isNull() {
            if (body != null)
                throw Exception("Body is not null")
        }

        fun isNotNull() {
            if (body == null)
                throw Exception("Body is null")
        }

        fun printBody(){
            if(body != null)
                println(body)
            else
                println("nothing")
        }
    }
}

class Client {
    fun perform(code: Int, body: String?) = ResponseActions(code, body)
}

fun main() {
    val mockClient = Client()
    val response = mockClient.perform(200, "OK")
        .andExpect {
            status {
                isOk()
                printStatus()
            }
            body {
                isNotNull()
                printBody()
            }
        }.andDo { response ->
            println(response)
        }
}
