package parser.search


import utils.durationToSeconds
import utils.extractThumbnail
import utils.findNodes
import kotlinx.serialization.json.*
import model.ArtistRef
import model.Song
import model.Track

object SearchParser {

    fun parse(root: JsonElement): List<Track> {

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
                        ?.get(
                            "musicResponsiveListItemFlexColumnRenderer"
                        )
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


                val subtitleRuns =
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

                val albumId =
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
                        ?.jsonObject
                        ?.get("navigationEndpoint")
                        ?.jsonObject
                        ?.get("browseEndpoint")
                        ?.jsonObject
                        ?.get("browseId")
                        ?.jsonPrimitive
                        ?.content

                val artists =
                    subtitleRuns
                        ?.takeWhile {

                            it.jsonObject["text"]
                                ?.jsonPrimitive
                                ?.content != " • "
                        }
                        ?.mapNotNull { run ->

                            val obj = run.jsonObject

                            val name =
                                obj["text"]
                                    ?.jsonPrimitive
                                    ?.content
                                    ?: return@mapNotNull null

                            if (
                                name == ", " ||
                                name == " & "
                            ) {
                                return@mapNotNull null
                            }

                            ArtistRef(
                                id =
                                    obj["navigationEndpoint"]
                                        ?.jsonObject
                                        ?.get("browseEndpoint")
                                        ?.jsonObject
                                        ?.get("browseId")
                                        ?.jsonPrimitive
                                        ?.content
                                        ?: "",
                                name = name
                            )
                        }
                        ?: emptyList()
                val album =
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
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonPrimitive
                        ?.content
                val duration =
                    subtitleRuns
                        ?.lastOrNull()
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonPrimitive
                        ?.content

                val durationSeconds =
                    durationToSeconds(duration)

                val track =
                    Track(
                        videoId = videoId,
                        title = title,
                        artists = artists,
                        album = album,
                        albumId = albumId ?: "",
                        duration = duration,
                        durationSeconds = durationSeconds,
                        thumbnailUrl = extractThumbnail(node),
                        plays = null,
                        trackNumber = null
                    )

                if (
                    track.durationSeconds != null &&
                    track.durationSeconds > 900
                ) {
                    return@mapNotNull null
                }

                track

            } catch (_: Exception) {
                null
            }
        }
    }
}