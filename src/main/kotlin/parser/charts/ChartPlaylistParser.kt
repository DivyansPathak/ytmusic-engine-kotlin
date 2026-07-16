package parser.charts

import kotlinx.serialization.json.*
import model.charts.ChartPlaylist
import utils.extractThumbnail

object ChartPlaylistParser {

    fun parse(
        item: JsonObject
    ): ChartPlaylist? {

        return try {

            val browseId =
                item["navigationEndpoint"]
                    ?.jsonObject
                    ?.get("browseEndpoint")
                    ?.jsonObject
                    ?.get("browseId")
                    ?.jsonPrimitive
                    ?.content
                    ?: return null

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
                    ?: return null

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

            ChartPlaylist(
                id = browseId,
                title = title,
                subtitle = subtitle,
                thumbnailUrl = extractThumbnail(item)
            )

        } catch (_: Exception) {
            null
        }
    }
}