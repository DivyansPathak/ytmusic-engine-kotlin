package parser.artist

import utils.extractThumbnail
import utils.findNodes
import kotlinx.serialization.json.*
import model.ArtistRef
import model.ArtistTopSong
import model.Track
import utils.durationToSeconds

object ArtistTopSongsParser {

    fun parse(
        root: JsonElement
    ): List<Track> {

        val nodes =
            findNodes(
                root,
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
                        ?.get("musicResponsiveListItemFlexColumnRenderer")
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonObject
                        ?.get("runs")
                        ?.jsonArray
                        ?.getOrNull(0)
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonPrimitive
                        ?.content
                        ?: "Unknown"

                // -------------------------
                // Artists
                // -------------------------

                val artistRuns =
                    node["flexColumns"]
                        ?.jsonArray
                        ?.getOrNull(1)
                        ?.jsonObject
                        ?.get("musicResponsiveListItemFlexColumnRenderer")
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonObject
                        ?.get("runs")
                        ?.jsonArray
                        ?: JsonArray(emptyList())

                val artistsWithIds =
                    artistRuns.mapNotNull { run ->

                        val obj = run.jsonObject

                        val artistId =
                            obj["navigationEndpoint"]
                                ?.jsonObject
                                ?.get("browseEndpoint")
                                ?.jsonObject
                                ?.get("browseId")
                                ?.jsonPrimitive
                                ?.content
                                ?: return@mapNotNull null

                        val artistName =
                            obj["text"]
                                ?.jsonPrimitive
                                ?.content
                                ?: return@mapNotNull null

                        ArtistRef(
                            id = artistId,
                            name = artistName
                        )
                    }

                val artists =
                    if (artistsWithIds.isNotEmpty()) {
                        artistsWithIds
                    } else {

                        val rawArtistText =
                            artistRuns
                                .firstOrNull()
                                ?.jsonObject
                                ?.get("text")
                                ?.jsonPrimitive
                                ?.content

                        rawArtistText
                            ?.replace(" and ", ", ")
                            ?.split(",")
                            ?.map { it.trim() }
                            ?.filter { it.isNotBlank() }
                            ?.map {
                                ArtistRef(
                                    id = "",
                                    name = it
                                )
                            }
                            ?: emptyList()
                    }

                // -------------------------
                // Plays
                // -------------------------

                val plays =
                    node["flexColumns"]
                        ?.jsonArray
                        ?.getOrNull(2)
                        ?.jsonObject
                        ?.get("musicResponsiveListItemFlexColumnRenderer")
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonObject
                        ?.get("runs")
                        ?.jsonArray
                        ?.getOrNull(0)
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonPrimitive
                        ?.content
// -------------------------
// Duration
// -------------------------

                val duration =
                    node["fixedColumns"]
                        ?.jsonArray
                        ?.firstOrNull()
                        ?.jsonObject
                        ?.get("musicResponsiveListItemFixedColumnRenderer")
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
                // -------------------------
                // Album
                // -------------------------

                val albumRun =
                    node["flexColumns"]
                        ?.jsonArray
                        ?.getOrNull(3)
                        ?.jsonObject
                        ?.get("musicResponsiveListItemFlexColumnRenderer")
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonObject
                        ?.get("runs")
                        ?.jsonArray
                        ?.getOrNull(0)
                        ?.jsonObject

                val album =
                    albumRun
                        ?.get("text")
                        ?.jsonPrimitive
                        ?.content

                val albumId =
                    albumRun
                        ?.get("navigationEndpoint")
                        ?.jsonObject
                        ?.get("browseEndpoint")
                        ?.jsonObject
                        ?.get("browseId")
                        ?.jsonPrimitive
                        ?.content
                Track(
                    videoId = videoId,
                    title = title,
                    artists = artists,
                    album = album,
                    albumId = albumId ?: "",
                    duration = duration,
                    durationSeconds = durationToSeconds(duration),
                    thumbnailUrl = extractThumbnail(node),
                    plays = plays,
                    trackNumber = null
                )


            } catch (_: Exception) {
                null
            }
        }
    }
}