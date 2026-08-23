package es.elchecf.app.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/** football-data.org v4 (CLAUDE.md §6). La API key se genera en build time — ver `generateApiKeys`. */
const val FOOTBALL_DATA_BASE_URL = "https://api.football-data.org/v4"

private const val REQUEST_TIMEOUT_MS = 10_000L

fun createHttpClient(): HttpClient =
    HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
        install(HttpTimeout) {
            requestTimeoutMillis = REQUEST_TIMEOUT_MS
        }
        defaultRequest {
            header("X-Auth-Token", ApiKeys.FOOTBALL_DATA_API_KEY)
        }
    }
