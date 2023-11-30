package com.mobile.expenseapp.service

import android.util.Log
import com.mobile.expenseapp.data.local.entity.LocalData
import com.mobile.expenseapp.service.entity.Login
import com.mobile.expenseapp.service.request.toSync
import com.mobile.expenseapp.service.response.LoginResponse
import com.mobile.expenseapp.service.response.SyncResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson


const val SERVER_URL = "http://157.230.194.15:8080"


object APIRepository {
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            gson()
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    suspend fun register(username: String, password: String): Boolean {
        return try {
            val response: HttpResponse = client.post("$SERVER_URL/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(Login(username, password))
            }
            when (response.status) {
                HttpStatusCode.OK -> true
                else -> false
            }
        } catch (e: Exception) {
            Log.e("APIRepository", e.message ?: "")
            false
        }
    }

    suspend fun login(username: String, password: String): Result<String> {
        return try {
            val response: HttpResponse = client.post("$SERVER_URL/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(Login(username, password))
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    val status: LoginResponse = response.body()
                    Result.success(status.token)
                }
                else -> Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun healthCheck(): Boolean {
        val response: HttpResponse = client.get("$SERVER_URL/healthcheck")
        return response.status == HttpStatusCode.OK
    }

    suspend fun syncGet(token: String): LocalData? {
        return try {
            val response: HttpResponse = client.get("$SERVER_URL/sync") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
            }
            response.body<SyncResponse>().data.toDto()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun syncPost(token: String, localData: LocalData): Boolean {
        return try {
            val syncRequest = localData.toSync()
            val response: HttpResponse = client.post("$SERVER_URL/sync") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(syncRequest)
            }

            println("Body: ${response.body<String>()}")
            response.status == HttpStatusCode.OK
        }catch(e: Exception){
            false
        }
    }
}