package parser.album

import utils.extractThumbnail
import utils.findNodes
import kotlinx.serialization.json.*
import model.AlbumHeader
import model.ArtistRef

object AlbumHeaderParser {

    fun parse(
        root: JsonElement,
        albumId: String
    ): AlbumHeader {

        val header =
            findNodes(
                root,
                "musicResponsiveHeaderRenderer"
            ).firstOrNull()
                ?: error("Album header not found")

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
                ?: "Unknown Album"

        val artistRuns =
            header["straplineTextOne"]
                ?.jsonObject
                ?.get("runs")
                ?.jsonArray

        val artists =
            artistRuns
                ?.mapNotNull { run ->

                    val obj = run.jsonObject

                    val artistName =
                        obj["text"]
                            ?.jsonPrimitive
                            ?.content
                            ?: return@mapNotNull null

                    val artistId =
                        obj["navigationEndpoint"]
                            ?.jsonObject
                            ?.get("browseEndpoint")
                            ?.jsonObject
                            ?.get("browseId")
                            ?.jsonPrimitive
                            ?.content
                            ?: return@mapNotNull null

                    ArtistRef(
                        id = artistId,
                        name = artistName
                    )
                }
                ?: emptyList()

        val subtitleRuns =
            header["subtitle"]
                ?.jsonObject
                ?.get("runs")
                ?.jsonArray

        val year =
            subtitleRuns
                ?.lastOrNull()
                ?.jsonObject
                ?.get("text")
                ?.jsonPrimitive
                ?.content

        val secondSubtitleRuns =
            header["secondSubtitle"]
                ?.jsonObject
                ?.get("runs")
                ?.jsonArray

        val songCount =
            secondSubtitleRuns
                ?.getOrNull(0)
                ?.jsonObject
                ?.get("text")
                ?.jsonPrimitive
                ?.content

        val duration =
            secondSubtitleRuns
                ?.getOrNull(2)
                ?.jsonObject
                ?.get("text")
                ?.jsonPrimitive
                ?.content

        val description =
            header["description"]
                ?.jsonObject
                ?.get("runs")
                ?.jsonArray
                ?.joinToString("") {
                    it.jsonObject["text"]
                        ?.jsonPrimitive
                        ?.content
                        ?: ""
                }

        val thumbnail =
            extractThumbnail(header)

        return AlbumHeader(
            id = albumId,
            title = title,
            artists = artists,
            year = year,
            songCount = songCount,
            duration = duration,
            thumbnailUrl = thumbnail,
            description = description
        )
    }
}