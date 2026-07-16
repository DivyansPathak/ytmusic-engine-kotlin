package parser.charts

import kotlinx.serialization.json.*
import model.charts.ChartArtist
import utils.extractThumbnail

object ChartArtistParser {

    fun parse(
        item: JsonObject
    ): ChartArtist? {

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

            val name =
                item["flexColumns"]
                    ?.jsonArray
                    ?.getOrNull(0)
                    ?.jsonObject
                    ?.get("musicResponsiveListItemFlexColumnRenderer")
                    ?.jsonObject
                    ?.get("text")
                    ?.jsonObject
                    ?.get("runs")
                    ?.jsonArray
                    ?.firstOrNull()
                    ?.jsonObject
                    ?.get("text")
                    ?.jsonPrimitive
                    ?.content
                    ?: return null

            val subscribers =
                item["flexColumns"]
                    ?.jsonArray
                    ?.getOrNull(1)
                    ?.jsonObject
                    ?.get("musicResponsiveListItemFlexColumnRenderer")
                    ?.jsonObject
                    ?.get("text")
                    ?.jsonObject
                    ?.get("runs")
                    ?.jsonArray
                    ?.firstOrNull()
                    ?.jsonObject
                    ?.get("text")
                    ?.jsonPrimitive
                    ?.content

            ChartArtist(
                id = browseId,
                name = name,
                subscribers = subscribers,
                thumbnailUrl = extractThumbnail(item)
            )

        } catch (_: Exception) {
            null
        }
    }
}