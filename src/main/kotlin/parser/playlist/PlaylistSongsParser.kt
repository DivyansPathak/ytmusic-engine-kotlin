package parser.playlist

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import model.AlbumSong
import model.Track
import utils.durationToSeconds
import utils.findNodes
import utils.parseArtistRefs

object PlaylistSongsParser {

    fun parse(
        root: JsonElement
    ): List<Track> {


        val items =
            findNodes(
                root,
                "musicResponsiveListItemRenderer"
            )

        return items.mapNotNull { item ->

            try {

                val videoId =
                    item["playlistItemData"]
                        ?.jsonObject
                        ?.get("videoId")
                        ?.jsonPrimitive
                        ?.content
                        ?: return@mapNotNull null

                val title =
                    item["flexColumns"]
                        ?.jsonArray
                        ?.getOrNull(0)
                        ?.jsonObject
                        ?.get(
                            "musicResponsiveListItemFlexColumnRenderer"
                        )
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
                        ?: return@mapNotNull null

                val artistRuns =
                    item["flexColumns"]
                        ?.jsonArray
                        ?.getOrNull(1)
                        ?.jsonObject
                        ?.get(
                            "musicResponsiveListItemFlexColumnRenderer"
                        )
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonObject
                        ?.get("runs")
                        ?.jsonArray

                val artists =
                    parseArtistRefs(
                        artistRuns
                    )

                val plays =
                    item["flexColumns"]
                        ?.jsonArray
                        ?.getOrNull(2)
                        ?.jsonObject
                        ?.get(
                            "musicResponsiveListItemFlexColumnRenderer"
                        )
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

                val duration =
                    item["fixedColumns"]
                        ?.jsonArray
                        ?.firstOrNull()
                        ?.jsonObject
                        ?.get(
                            "musicResponsiveListItemFixedColumnRenderer"
                        )
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

                val trackNumber =
                    item["index"]
                        ?.jsonObject
                        ?.get("runs")
                        ?.jsonArray
                        ?.firstOrNull()
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonPrimitive
                        ?.content
                        ?.toIntOrNull()

                Track(
                    videoId = videoId,
                    title = title,
                    artists = artists,
                    album = null,
                    albumId = "",
                    duration = duration,
                    durationSeconds = durationToSeconds(duration),
                    thumbnailUrl = "https://i.ytimg.com/vi/$videoId/hqdefault.jpg",
                    plays = plays,
                    trackNumber = trackNumber
                )

            } catch (_: Exception) {
                null
            }
        }
    }
}