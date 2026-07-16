package parser.artist

import utils.extractThumbnail
import utils.findNodes
import kotlinx.serialization.json.*
import model.Artist

object ArtistParser {

    fun parse(root: JsonElement): List<Artist> {

        val nodes =
            findNodes(
                root,
                "musicResponsiveListItemRenderer"
            )

        return nodes.mapNotNull { node ->

            try {

                val name =
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
                        ?: return@mapNotNull null

                val artistId =
                    node["navigationEndpoint"]
                        ?.jsonObject
                        ?.get("browseEndpoint")
                        ?.jsonObject
                        ?.get("browseId")
                        ?.jsonPrimitive
                        ?.content
                        ?: return@mapNotNull null

                val monthlyAudience =
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
                        ?.getOrNull(2)
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonPrimitive
                        ?.content

                Artist(
                    id = artistId,
                    name = name,
                    monthlyAudience = monthlyAudience,
                    thumbnailUrl = extractThumbnail(node)
                )

            } catch (_: Exception) {
                null
            }
        }
    }
}