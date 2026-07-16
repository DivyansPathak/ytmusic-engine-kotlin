package parser.mood

import kotlinx.serialization.json.*
import model.ArtistRef
import model.Song
import utils.durationToSeconds
import utils.extractThumbnail
import utils.findNodes
import utils.parseArtistRefs

object MoodSongsParser {

    fun parse(
        shelf: JsonObject
    ): List<Song> {

        val nodes =
            findNodes(
                shelf,
                "musicResponsiveListItemRenderer"
            )

        return nodes.mapNotNull { node ->

            try {

                val videoId =
                    node["playlistItemData"]
                        ?.jsonObject
                        ?.get("videoId")
                        ?.jsonPrimitive
                        ?.content
                        ?: return@mapNotNull null

                val title =
                    node["flexColumns"]
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

                val subtitleRuns =
                    node["flexColumns"]
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
                        subtitleRuns
                    )

                val albumRun =
                    subtitleRuns
                        ?.firstOrNull {

                            it.jsonObject
                                .get("navigationEndpoint")
                                ?.jsonObject
                                ?.get("browseEndpoint")
                                ?.jsonObject
                                ?.get("browseId")
                                ?.jsonPrimitive
                                ?.content
                                ?.startsWith("MPRE") == true
                        }

                val album =
                    albumRun
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonPrimitive
                        ?.content

                val albumId =
                    albumRun
                        ?.jsonObject
                        ?.get("navigationEndpoint")
                        ?.jsonObject
                        ?.get("browseEndpoint")
                        ?.jsonObject
                        ?.get("browseId")
                        ?.jsonPrimitive
                        ?.content
                        ?: ""

                val duration =
                    node["fixedColumns"]
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

                Song(
                    videoId = videoId,
                    title = title,
                    artists = artists,
                    album = album,
                    albumId = albumId,
                    duration = duration,
                    durationSeconds =
                        durationToSeconds(
                            duration
                        ),
                    thumbnailUrl =
                        extractThumbnail(node)
                )

            } catch (_: Exception) {
                null
            }
        }
    }
}