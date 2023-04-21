package com.example.assignment

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


@Serializable
data class feedback (val firstname: String?="",
                     val last_name:String?="",
                     val token: String?="",
                     val error: String?="")
@Serializable
data class feedbackborrow (val message: String?="",
                           val error: String?="")
object KtorClient {
    var token: String = ""

    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json{
                explicitNulls = false
            }) // enable the client to perform JSON serialization
        }
        install(Logging)
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            header("Authorization", token)
        }
    }


    suspend fun logpostFeedback(c: Credential): String {
        val response: feedback = httpClient.post("http://comp4107.azurewebsites.net/user/login") {
            setBody(c)
        }.body()
        token = response.token ?: "no token"
        if(response.error==""){
            return response.firstname.toString()+","+ response.last_name.toString()+" Log successful"+"\n"+token
        }else return response.error.toString()
    }

    suspend fun borrowpostFeedback(itemId: String): String {
        try {
            val responseContent: String = httpClient.post("http://comp4107.azurewebsites.net/user/borrow/${itemId}") {
//                setBody(itemId)
            }.body()

            val response = Json.decodeFromString<feedbackborrow>(responseContent)
            if (response.error == "") {
                return response.message.toString()
            } else {
                return response.error.toString()
            }
        } catch (e: Exception) {
            e.message?.let { Log.e("Error", it) }
            return "An error occurred while processing your request."
        }
    }

    suspend fun returnpostFeedback(itemId: String): String {
        try {
            val responseContent: String = httpClient.post("http://comp4107.azurewebsites.net/user/return/${itemId}") {
//                setBody(itemId)
            }.body()

            val response = Json.decodeFromString<feedbackborrow>(responseContent)
            if (response.error == "") {
                return response.message.toString()
            } else {
                return response.error.toString()
            }
        } catch (e: Exception) {
            e.message?.let { Log.e("Error", it) }
            return "An error occurred while processing your request."
        }
    }

    suspend fun consumepostFeedback(itemId: String): String {
        try {
            val responseContent: String = httpClient.post("http://comp4107.azurewebsites.net/user/consume/${itemId}") {
//                setBody(itemId)
            }.body()

            val response = Json.decodeFromString<feedbackborrow>(responseContent)
            if (response.error == "") {
                return response.message.toString()
            } else {
                return response.error.toString()
            }
        } catch (e: Exception) {
            e.message?.let { Log.e("Error", it) }
            return "An error occurred while processing your request."
        }
    }



    data class FeedsResponsebook(
        val feeds: List<Feedbook>,
    )
    data class FeedsResponsegame(
        val feeds: List<Feedgame>,
    )
    data class FeedsResponsegift(
        val feeds: List<Feedgift>,
    )
    data class FeedsResponsematerial(
        val feeds: List<Feedmaterial>,
    )

    suspend fun getFeeds1(page: Int, limit: Int): FeedsResponsebook {
        val response = httpClient.get("http://comp4107.azurewebsites.net/inventory?page=$page&limit=$limit&type=book")
        return FeedsResponsebook(
            feeds = response.body(),
        )
    }
    suspend fun getFeeds2(page: Int, limit: Int): FeedsResponsegame {
        val response = httpClient.get("http://comp4107.azurewebsites.net/inventory?page=$page&limit=$limit&type=game")
        return FeedsResponsegame(
            feeds = response.body(),
        )
    }
    suspend fun getFeeds3(page: Int, limit: Int): FeedsResponsegift {
        val response = httpClient.get("http://comp4107.azurewebsites.net/inventory?page=$page&limit=$limit&type=gift")
        return FeedsResponsegift(
            feeds = response.body(),
        )
    }
    suspend fun getFeeds4(page: Int, limit: Int): FeedsResponsematerial {
        val response = httpClient.get("http://comp4107.azurewebsites.net/inventory?page=$page&limit=$limit&type=material")
        return FeedsResponsematerial(
            feeds = response.body(),
        )
    }
}