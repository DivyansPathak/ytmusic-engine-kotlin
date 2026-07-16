package parser.artist

import utils.extractThumbnail
import utils.findNodes
import kotlinx.serialization.json.*
import model.ArtistVideo

object ArtistVideosParser {

    fun parse(
        root: JsonElement
    ): List<ArtistVideo> {

        val shelves =
            findNodes(
                root,
                "musicCarouselShelfRenderer"
            )

        val videosShelf =
            shelves.firstOrNull {

                it["header"]
                    ?.jsonObject
                    ?.toString()
                    ?.contains("Videos"
                    ) == true
            }
                ?: return emptyList()
        val items =
            findNodes(
                videosShelf,
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

                val videoId =
                    item["thumbnailOverlay"]
                        ?.jsonObject
                        ?.get("musicItemThumbnailOverlayRenderer")
                        ?.jsonObject
                        ?.get("content")
                        ?.jsonObject
                        ?.get("musicPlayButtonRenderer")
                        ?.jsonObject
                        ?.get("playNavigationEndpoint")
                        ?.jsonObject
                        ?.get("watchEndpoint")
                        ?.jsonObject
                        ?.get("videoId")
                        ?.jsonPrimitive
                        ?.content
                        ?: return@mapNotNull null

                val runs =
                    item["subtitle"]
                        ?.jsonObject
                        ?.get("runs")
                        ?.jsonArray

                val duration =
                    runs
                        ?.getOrNull(0)
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonPrimitive
                        ?.content

                val artist =
                    runs
                        ?.getOrNull(2)
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonPrimitive
                        ?.content

                ArtistVideo(
                    videoId = videoId,
                    title = title,
                    duration = duration,
                    artist = artist,
                    thumbnailUrl = extractThumbnail(item)
                )

            } catch (_: Exception) {
                null
            }
        }
    }
}