package parser.artist

import utils.extractThumbnail
import utils.findNodes
import kotlinx.serialization.json.*
import model.AlbumPreview

object ArtistAlbumsParser {

    fun parse(
        root: JsonElement
    ): List<AlbumPreview> {

        val shelves =
            findNodes(
                root,
                "musicCarouselShelfRenderer"
            )

        val albumsShelf =
            shelves.firstOrNull {

                it["header"]
                    ?.jsonObject
                    ?.toString()
                    ?.contains("Albums") == true
            }

        val items =
            findNodes(
                albumsShelf ?: return emptyList(),
                "musicTwoRowItemRenderer"
            )

        return items.mapNotNull { item ->
            try {

                val title =
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

                val albumId =
                    item["navigationEndpoint"]
                        ?.jsonObject
                        ?.get("browseEndpoint")
                        ?.jsonObject
                        ?.get("browseId")
                        ?.jsonPrimitive
                        ?.content
                        ?: return@mapNotNull null

                val year =
                    item["subtitle"]
                        ?.jsonObject
                        ?.get("runs")
                        ?.jsonArray
                        ?.firstOrNull()
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonPrimitive
                        ?.content

                val thumbnail =
                    extractThumbnail(item)

                AlbumPreview(
                    id = albumId,
                    title = title,
                    year = year,
                    thumbnailUrl = thumbnail
                )

            } catch (_: Exception) {
                null
            }
        }
    }
}
