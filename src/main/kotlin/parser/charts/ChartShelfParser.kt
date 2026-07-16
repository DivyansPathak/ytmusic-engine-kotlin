package parser.charts

import kotlinx.serialization.json.*
import model.charts.ChartShelf
import utils.findNodes

object ChartShelfParser {

    fun parse(
        shelf: JsonObject
    ): ChartShelf {

        val title =
            shelf["header"]
                ?.jsonObject
                ?.get("musicCarouselShelfBasicHeaderRenderer")
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

        val playlists =
            findNodes(
                shelf,
                "musicTwoRowItemRenderer"
            ).mapNotNull {
                ChartPlaylistParser.parse(it)
            }

        val artists =
            findNodes(
                shelf,
                "musicResponsiveListItemRenderer"
            ).mapNotNull {
                ChartArtistParser.parse(it)
            }

        return ChartShelf(
            title = title,
            playlists = playlists,
            artists = artists
        )
    }
}