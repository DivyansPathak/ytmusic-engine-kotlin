package parser.mood

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import model.mood.Mood
import model.mood.MoodSection
import utils.findNodes

object MoodsGenresParser {

    fun parse(
        root: JsonElement
    ): List<MoodSection> {

        val sections =
            findNodes(
                root,
                "gridRenderer"
            )

        return sections.mapNotNull { section ->

            try {

                val title =
                    section["header"]
                        ?.jsonObject
                        ?.get("gridHeaderRenderer")
                        ?.jsonObject
                        ?.get("title")
                        ?.jsonObject
                        ?.get("runs")
                        ?.jsonArray
                        ?.firstOrNull()
                        ?.jsonObject
                        ?.get("text")
                        ?.jsonPrimitive
                        ?.content
                        ?: return@mapNotNull null

                val buttons =
                    findNodes(
                        section,
                        "musicNavigationButtonRenderer"
                    )

                val moods =
                    buttons.mapNotNull { button ->

                        try {

                            val moodTitle =
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

                            val browse =
                                button["clickCommand"]
                                    ?.jsonObject
                                    ?.get("browseEndpoint")
                                    ?.jsonObject
                                    ?: return@mapNotNull null

                            Mood(
                                title = moodTitle,
                                browseId =
                                    browse["browseId"]
                                        ?.jsonPrimitive
                                        ?.content
                                        ?: return@mapNotNull null,
                                params =
                                    browse["params"]
                                        ?.jsonPrimitive
                                        ?.content
                            )

                        } catch (_: Exception) {
                            null
                        }
                    }

                MoodSection(
                    title = title,
                    moods = moods
                )

            } catch (_: Exception) {
                null
            }
        }
    }
}