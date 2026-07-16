package parser.artist

import utils.extractThumbnail
import utils.findNodes
import kotlinx.serialization.json.*
import model.ArtistPreview

object ArtistFansAlsoLikeParser {

    fun parse(
        root: JsonElement
    ): List<ArtistPreview> {

        val shelves =
            findNodes(
                root,
                "musicCarouselShelfRenderer"
            )

        val targetShelf =
            shelves.firstOrNull {

                it["header"]
                    ?.toString()
                    ?.contains(
                        "Fans might also like",
                        true
                    ) == true
            }
                ?: return emptyList()
        val items =
            findNodes(
                targetShelf,
                "musicTwoRowItemRenderer"
            )

        return items.mapNotNull { item ->

            try {

                val name =
                    item["title"]
                        ?.jsonObject
                        ?.get("runs")
                        ?.jsonArray
                        ?.firstOrNull()
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonPrimitive
                        ?.content
                        ?: return@mapNotNull null

                val artistId =
                    item["navigationEndpoint"]
                        ?.jsonObject
                        ?.get("browseEndpoint")
                        ?.jsonObject
                        ?.get("browseId")
                        ?.jsonPrimitive
                        ?.content
                        ?: return@mapNotNull null

                ArtistPreview(
                    id = artistId,
                    name = name,
                    thumbnailUrl =
                        extractThumbnail(item)
                )

            } catch (_: Exception) {
                null
            }
        }
    }
}