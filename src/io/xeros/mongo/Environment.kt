package io.xeros.mongo

val DEFAULT_ENVIRONMENT = Environment(
    database = "RuneLore",
    host = "localhost",
    username = null,
    password = null,
    options = null
)


val CLOUD_ENVIRONMENT = Environment(
    database = "pyron",
    host = "pyron.bu8rbim.mongodb.net",
    username = "pyronrsps",
    password = "tbY6EcNVnC5vJtT4",
    options = "ssl=true&authSource=admin&retryWrites=true&w=majority"
)

data class Environment(val database : String, val host : String, val port : Int = 27017, val username : String?, val password : String?, val options : String?) {

    fun getConnectionLink() : String {
        return if(username == null || password == null) {
            "mongodb+srv://$host/$database"
        } else {
            "mongodb+srv://${username}:${password}@${host}/${database}?${options}"
        }
    }

    fun getAsyncLink(): String {
        return "mongodb+srv://$username:$password@${host}/?retryWrites=true&w=majority"
    }

}
