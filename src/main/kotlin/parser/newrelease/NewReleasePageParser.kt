package parser.newrelease

import kotlinx.serialization.json.*
import model.newrelease.NewReleaseAlbum
import model.newrelease.NewReleasePage
import utils.*

object NewReleasePageParser {

    fun parse(
        root: JsonElement
    ): NewReleasePage {

        val items =
            findNodes(
                root,
                "musicTwoRowItemRenderer"
            )

        val albums =
            items.mapNotNull { item ->

                try {

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
                            ?: return@mapNotNull null

                    val browseId =
                        item["navigationEndpoint"]
                            ?.jsonObject
                            ?.get("browseEndpoint")
                            ?.jsonObject
                            ?.get("browseId")
                            ?.jsonPrimitive
                            ?.content
                            ?: return@mapNotNull null

                    val subtitleRuns =
                        item["subtitle"]
                            ?.jsonObject
                            ?.get("runs")
                            ?.jsonArray

                    val artists =
                        parseArtistRefs(
                            subtitleRuns
                        )

                    NewReleaseAlbum(
                        id = browseId,
                        title = title,
                        artists = artists,
                        thumbnailUrl =
                            extractThumbnail(item)
                    )

                } catch (_: Exception) {
                    null
                }
            }

        return NewReleasePage(
            albums = albums
        )
    }
}