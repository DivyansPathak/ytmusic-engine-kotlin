package parser.artist

import utils.findNodes
import kotlinx.serialization.json.*
import model.ArtistHeader

object ArtistHeaderParser {

    fun parse(
        root: JsonElement,
        artistId: String
    ): ArtistHeader {

        val microformat =
            findNodes(
                root,
                "microformatDataRenderer"
            ).firstOrNull()
                ?: return ArtistHeader(
                    id = artistId,
                    name = "Unknown",
                    description = null,
                    monthlyAudience = null,
                    thumbnailUrl = null,
                    channelUrl = null
                )

        val name =
            microformat["title"]
                ?.jsonPrimitive
                ?.content
                ?: "Unknown"

        val description =
            microformat["description"]
                ?.jsonPrimitive
                ?.content

        val channelUrl =
            microformat["urlCanonical"]
                ?.jsonPrimitive
                ?.content

        val thumbnailUrl =
            microformat["thumbnail"]
                ?.jsonObject
                ?.get("thumbnails")
                ?.jsonArray
                ?.lastOrNull()
                ?.jsonObject
                ?.get("url")
                ?.jsonPrimitive
                ?.content

        return ArtistHeader(
            id = artistId,
            name = name,
            description = description,
            monthlyAudience = extractMonthlyAudience(root),
            thumbnailUrl = thumbnailUrl,
            channelUrl = channelUrl
        )
    }

    private fun extractMonthlyAudience(
        root: JsonElement
    ): String? {

        val rootString = root.toString()

        val regex =
            """"monthlyListenerCount":\{"runs":\[\{"text":"([^"]+)""""
                .toRegex()

        return regex
            .find(rootString)
            ?.groupValues
            ?.getOrNull(1)
    }
}