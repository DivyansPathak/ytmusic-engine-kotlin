package parser.playlist

import kotlinx.serialization.json.*
import model.playlist.PlaylistHeader
import utils.extractThumbnail
import utils.findNodes

object PlaylistHeaderParser {

    fun parse(
        root: JsonElement,
        playlistId: String
    ): PlaylistHeader {
        val headers =
            findNodes(
                root,
                "musicResponsiveHeaderRenderer"
            )

//        println("Headers found: ${headers.size}")
//        println(headers.firstOrNull()?.keys)
//        headers.firstOrNull()?.let {
//            println(it.keys)
//        }

        val header =
            findNodes(
                root,
                "musicResponsiveHeaderRenderer"
            ).firstOrNull()
                ?: error("Playlist header not found")

        println("===== PLAYLIST HEADER RAW =====")
        println(header.keys)

        val title =
            header["title"]
                ?.jsonObject
                ?.get("runs")
                ?.jsonArray
                ?.firstOrNull()
                ?.jsonObject
                ?.get("text")
                ?.jsonPrimitive
                ?.content
                ?: ""

        val author =
            header["facepile"]
                ?.jsonObject
                ?.get("avatarStackViewModel")
                ?.jsonObject
                ?.get("text")
                ?.jsonObject
                ?.get("content")
                ?.jsonPrimitive
                ?.content

        val secondSubtitleRuns =
            header["secondSubtitle"]
                ?.jsonObject
                ?.get("runs")
                ?.jsonArray

        val songCount =
            secondSubtitleRuns
                ?.getOrNull(2)
                ?.jsonObject
                ?.get("text")
                ?.jsonPrimitive
                ?.content

        val duration =
            secondSubtitleRuns
                ?.getOrNull(4)
                ?.jsonObject
                ?.get("text")
                ?.jsonPrimitive
                ?.content

        return PlaylistHeader(
            id = playlistId,
            title = title,
            author = author,
            songCount = songCount,
            duration = duration,
            thumbnailUrl =
                extractThumbnail(header),
            description = null
        )
    }
}