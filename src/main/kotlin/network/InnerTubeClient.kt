package network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.*
import model.modelUtils.SearchFilter

class InnerTubeClient {

    companion object {
        private const val BASE_URL =
            "https://music.youtube.com/youtubei/v1"

        private const val CLIENT_NAME =
            "WEB_REMIX"

        private const val CLIENT_VERSION =
            "1.20250618.01.00"
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val client = HttpClient(CIO) {

        install(HttpTimeout) {
            requestTimeoutMillis = 60_000
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = 60_000
        }

        expectSuccess = false
    }

    suspend fun search(
        query: String,
        filter : SearchFilter = SearchFilter.SONGS): JsonElement {

        val payload = buildJsonObject {

            putJsonObject("context") {

                putJsonObject("client") {

                    put("clientName", CLIENT_NAME)
                    put("clientVersion", CLIENT_VERSION)
                    put("hl", "en")
                }
            }

            put("query", query)

            put(
                "params",
                filter.params
            )
        }

        val response = client.post("$BASE_URL/search") {

            contentType(ContentType.Application.Json)

            header(
                HttpHeaders.UserAgent,
                "Mozilla/5.0"
            )

            header(
                HttpHeaders.Origin,
                "https://music.youtube.com"
            )

            setBody(payload.toString())
        }

        return json.parseToJsonElement(
            response.bodyAsText()
        )
    }

    suspend fun next(videoId: String): JsonElement {

        val payload = buildJsonObject {

            putJsonObject("context") {

                putJsonObject("client") {

                    put("clientName", CLIENT_NAME)
                    put("clientVersion", CLIENT_VERSION)
                    put("hl", "en")
                }
            }

            put("videoId", videoId)

            put(
                "playlistId",
                "RDAMVM$videoId"
            )
        }

        val response = client.post("$BASE_URL/next") {

            contentType(ContentType.Application.Json)

            header(
                HttpHeaders.UserAgent,
                "Mozilla/5.0"
            )

            header(
                HttpHeaders.Origin,
                "https://music.youtube.com"
            )

            setBody(payload.toString())
        }

        return json.parseToJsonElement(
            response.bodyAsText()
        )
    }
    suspend fun browse(
        browseId: String,
        params: String? = null
    ): JsonElement {

        repeat(3) {attempt ->
            try {
                val payload = buildJsonObject {

                    putJsonObject("context") {

                        putJsonObject("client") {

                            put("clientName", CLIENT_NAME)
                            put("clientVersion", CLIENT_VERSION)
                            put("hl", "en")
                        }
                    }

                    put("browseId", browseId)

                    params?.let {
                        put("params", it)
                    }
                }

                val response =
                    client.post("$BASE_URL/browse") {

                        contentType(
                            ContentType.Application.Json
                        )

                        header(
                            HttpHeaders.UserAgent,
                            "Mozilla/5.0"
                        )

                        header(
                            HttpHeaders.Origin,
                            "https://music.youtube.com"
                        )

                        setBody(
                            payload.toString()
                        )
                    }

                return json.parseToJsonElement(
                    response.bodyAsText()
                )
            } catch (e:Exception){
                if (attempt == 2) throw e

                delay(1000L * (attempt + 1))
            }
        }
    error("Unreachable")
    }
    suspend fun searchSuggestions(
        query: String
    ): List<String> {

        if (query.isBlank()) {
            return emptyList()
        }

        val response =
            client.get(
                "https://suggestqueries.google.com/complete/search"
            ) {

                parameter("client", "firefox")
                parameter("ds", "yt")
                parameter("q", query)
            }

        val json =
            json.parseToJsonElement(
                response.bodyAsText()
            ).jsonArray

        if (json.size < 2) {
            return emptyList()
        }

        return json[1]
            .jsonArray
            .map {
                it.jsonPrimitive.content
            }
    }

    fun close() {
        client.close()
    }
}