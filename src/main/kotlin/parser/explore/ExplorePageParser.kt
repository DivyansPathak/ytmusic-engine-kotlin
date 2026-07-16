package parser.explore

import kotlinx.serialization.json.*
import model.explore.ExplorePage
import model.explore.ExploreSection
import utils.findNodes

object ExplorePageParser {

    fun parse(
        root: JsonElement
    ): ExplorePage {

        val shelves =
            findNodes(
                root,
                "musicCarouselShelfRenderer"
            )

        val sections =
            shelves.mapNotNull { shelf ->

                try {

                    val header =
                        shelf["header"]
                            ?.jsonObject
                            ?.get(
                                "musicCarouselShelfBasicHeaderRenderer"
                            )
                            ?.jsonObject
                            ?: return@mapNotNull null

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
                            ?: return@mapNotNull null

                    val browseEndpoint =
                        header["title"]
                            ?.jsonObject
                            ?.get("runs")
                            ?.jsonArray
                            ?.firstOrNull()
                            ?.jsonObject
                            ?.get("navigationEndpoint")
                            ?.jsonObject
                            ?.get("browseEndpoint")
                            ?.jsonObject

                    val browseId =
                        browseEndpoint
                            ?.get("browseId")
                            ?.jsonPrimitive
                            ?.content

                    val params =
                        browseEndpoint
                            ?.get("params")
                            ?.jsonPrimitive
                            ?.content

                    ExploreSection(
                        title = title,
                        browseId = browseId,
                        params = params
                    )

                } catch (_: Exception) {
                    null
                }
            }

        return ExplorePage(
            sections = sections
        )
    }
}