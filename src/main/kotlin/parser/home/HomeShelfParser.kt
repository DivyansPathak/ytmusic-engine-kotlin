package parser.home

import kotlinx.serialization.json.*
import model.home.HomeItem
import model.home.HomeShelf
import utils.extractThumbnail
import utils.findNodes

object HomeShelfParser {

    fun parse(
        shelf: JsonObject
    ): HomeShelf {

        val title =
            shelf["header"]
                ?.jsonObject
                ?.get(
                    "musicCarouselShelfBasicHeaderRenderer"
                )
                ?.jsonObject
                ?.get("title")
                ?.jsonObject
                ?.get("runs")
                ?.jsonArray
                ?.firstOrNull()
                ?.jsonObject
                ?.get("text")
                ?.jsonPrimitive
                ?.content
                ?: "Unknown"

        val items =
            findNodes(
                shelf,
                "musicTwoRowItemRenderer"
            ).mapNotNull { item ->

                try {

                    val browseId =
                        item["navigationEndpoint"]
                            ?.jsonObject
                            ?.get("browseEndpoint")
                            ?.jsonObject
                            ?.get("browseId")
                            ?.jsonPrimitive
                            ?.content

                    val playlistId =
                        browseId
                            ?.removePrefix("VL")

                    val titleText =
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

                    val subtitle =
                        item["subtitle"]
                            ?.jsonObject
                            ?.get("runs")
                            ?.jsonArray
                            ?.joinToString("") {
                                it.jsonObject["text"]
                                    ?.jsonPrimitive
                                    ?.content
                                    ?: ""
                            }

                    HomeItem(
                        id = playlistId ?: "",
                        title = titleText,
                        subtitle = subtitle,
                        thumbnailUrl =
                            extractThumbnail(item)
                    )

                } catch (_: Exception) {
                    null
                }
            }
        return HomeShelf(
            title = title,
            items = items
        )
    }
}