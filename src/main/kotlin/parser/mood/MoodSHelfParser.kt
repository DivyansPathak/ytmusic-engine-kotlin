package parser.mood

import kotlinx.serialization.json.*
import model.home.HomeItem
import model.mood.MoodShelf
import utils.extractThumbnail
import utils.findNodes

object MoodShelfParser {
    fun parse(
        shelf: JsonObject
    ): MoodShelf {

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
                            ?: return@mapNotNull null

                    val itemTitle =
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
                        id = browseId,
                        title = itemTitle,
                        subtitle = subtitle,
                        thumbnailUrl =
                            extractThumbnail(item)
                    )

                } catch (_: Exception) {
                    null
                }
            }

        val songs =
            MoodSongsParser.parse(
                shelf
            )

        return MoodShelf(
            title = title,
            items = items,
            songs = songs
        )
    }
}