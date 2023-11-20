package com.mobile.expenseapp.service

import com.mobile.expenseapp.service.entity.AccountSync
import com.mobile.expenseapp.service.entity.TransactionSync
import com.mobile.expenseapp.service.request.SyncRequest
import com.mobile.expenseapp.service.request.toSync
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class APIRespositoryTest {
    @Test
    fun `check connectivity with server`() {
        assert(runBlocking {
            APIRepository.healthCheck()
        })
    }

    private fun generateRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    @Test
    fun `registering a random user`() {
        val randomUsername = generateRandomString(10)
        val randomPassword = generateRandomString(10)

        assert(runBlocking {
            APIRepository.register(
                randomUsername,
                randomPassword
            )
        })
    }

    @Test
    fun `loging in with existing credential`() = runBlocking {
        val username = "nam7v3"
        val password = "nam7v3"

        assert(APIRepository.login(username, password) != null)
    }

    @Test
    fun `registering an user and try syncing data`() = runBlocking {
        val username = generateRandomString(10)
        val password = generateRandomString(10)

        assert(APIRepository.register(username, password))
        val token = APIRepository.login(username, password) ?: throw AssertionError("No token was sent")
        println("TOKEN: $token")
        val syncRequest = SyncRequest(
            listOf(
                AccountSync(1, "Cash", 32.0, 10.0, 2.0),
                AccountSync(2, "Card", 23.0, 32.0, 1.0),
                AccountSync(3, "Bank", 43.0, 12.0, 5.0),
            ),
            listOf(
                TransactionSync(
                    100000,
                    "Sunday",
                    10.0,
                    "Cash",
                    "Food",
                    "OOGa BOooga",
                    "Monke"
                ),
                TransactionSync(
                    100203,
                    "Sunday",
                    10.0,
                    "Bank",
                    "Cars",
                    "OOGa BOooga",
                    "Dooge"
                ),
            )
        )

        assert(APIRepository.syncPost(token, syncRequest.toDto()))

        val syncResponse = APIRepository.syncGet(token) ?: throw AssertionError("Failed sync request")
        println(syncResponse)
        assert(syncResponse.toSync() == syncRequest)
    }
}