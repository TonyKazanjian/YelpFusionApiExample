package com.tonykazanjian.sonyyelpfusion

/**
 * @author Tony Kazanjian
 */
class ApiUtils {
    companion object{
        const val BASE_URL = "https://api.yelp.com/v3/"
        const val AUTHORIZATION_HEADER = "Authorization"
        private const val AUTHORIZATION_TOKEN = "Bearer %s"
        const val API_TOKEN = "q_tomjx9kHXNolNAQDk-ieNJog7rF0pINoykzGz1RG2OdlQQh9QTaX0Gt7E8NBzV7-W6Gid7m0wlGiSoBmkEH8E8ZDkervRd9N84rzbUTt0Z66Z6jVfPbcqo_BAoXXYx"

        fun createHeader(): String{
            return String.format(AUTHORIZATION_TOKEN, API_TOKEN)
        }
    }
}


