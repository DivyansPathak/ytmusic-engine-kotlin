package parser.mood

import kotlinx.serialization.json.*
import model.mood.Mood
import utils.findNodes

object MoodsGenresParser {

    fun parse(
        root: JsonElement
    ): List<Mood> {

        val buttons =
            findNodes(
                root,
                "musicNavigationButtonRenderer"
            )

        return buttons.mapNotNull { button ->

            try {

                val title =
                    button["buttonText"]
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
                    button["clickCommand"]
                        ?.jsonObject
                        ?.get("browseEndpoint")
                        ?.jsonObject
                        ?: return@mapNotNull null

                val browseId =
                    browseEndpoint["browseId"]
                        ?.jsonPrimitive
                        ?.content
                        ?: return@mapNotNull null

                val params =
                    browseEndpoint["params"]
                        ?.jsonPrimitive
                        ?.content

                Mood(
                    title = title,
                    browseId = browseId,
                    params = params
                )

            } catch (_: Exception) {
                null
            }
        }
    }
}