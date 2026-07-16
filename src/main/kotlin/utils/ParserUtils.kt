package utils

import kotlinx.serialization.json.*
import model.ArtistRef

fun findNodes(
    element: JsonElement,
    targetKey: String
): List<JsonObject> {

    val results = mutableListOf<JsonObject>()

    when (element) {

        is JsonObject -> {

            element[targetKey]
                ?.jsonObject
                ?.let(results::add)

            element.values.forEach {
                results += findNodes(
                    it,
                    targetKey
                )
            }
        }

        is JsonArray -> {

            element.forEach {
                results += findNodes(
                    it,
                    targetKey
                )
            }
        }

        else -> {}
    }

    return results
}

fun extractThumbnail(
    node: JsonObject
): String {

    val twoRowThumb =
        node["thumbnailRenderer"]
            ?.jsonObject
            ?.get("musicThumbnailRenderer")
            ?.jsonObject
            ?.get("thumbnail")
            ?.jsonObject
            ?.get("thumbnails")
            ?.jsonArray
            ?.lastOrNull()
            ?.jsonObject
            ?.get("url")
            ?.jsonPrimitive
            ?.content

    if (twoRowThumb != null) {
        return twoRowThumb
    }

    val thumbnails =

        node["thumbnail"]
            ?.jsonObject
            ?.get("musicThumbnailRenderer")
            ?.jsonObject
            ?.get("thumbnail")
            ?.jsonObject
            ?.get("thumbnails")
            ?.jsonArray
            ?: node["thumbnail"]
                ?.jsonObject
                ?.get("thumbnails")
                ?.jsonArray

    val thumb =
        thumbnails?.maxByOrNull {

            it.jsonObject["width"]
                ?.jsonPrimitive
                ?.int ?: 0
        }

    return thumb
        ?.jsonObject
        ?.get("url")
        ?.jsonPrimitive
        ?.content
        ?.replace(
            Regex("=w\\d+-h\\d+.*"),
            "=w1080-h1080"
        )
        ?: ""
}

fun parseArtistRefs(
    runs: JsonArray?
): List<ArtistRef> {

    if (runs == null) {
        return emptyList()
    }

    return runs.mapNotNull { run ->

        try {

            val obj = run.jsonObject

            val name =
                obj["text"]
                    ?.jsonPrimitive
                    ?.content
                    ?: return@mapNotNull null

            val artistId =
                obj["navigationEndpoint"]
                    ?.jsonObject
                    ?.get("browseEndpoint")
                    ?.jsonObject
                    ?.get("browseId")
                    ?.jsonPrimitive
                    ?.content

            if (
                artistId == null ||
                !artistId.startsWith("UC")
            ) {
                return@mapNotNull null
            }

            ArtistRef(
                id = artistId,
                name = name
            )

        } catch (_: Exception) {
            null
        }
    }
}

fun test123(): String {
    return "hello"
}