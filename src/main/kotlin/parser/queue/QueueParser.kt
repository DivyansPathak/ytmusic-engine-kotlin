package parser.queue



import utils.extractThumbnail
import utils.findNodes
import kotlinx.serialization.json.*
import model.QueueItem

object QueueParser {

    fun parse(root: JsonElement): List<QueueItem> {

        val nodes =
            findNodes(
                root,
                "playlistPanelVideoRenderer"
            )

        return nodes.mapNotNull { node ->

            try {

                QueueItem(
                    videoId =
                        node["videoId"]
                            ?.jsonPrimitive
                            ?.content
                            ?: return@mapNotNull null,

                    title =
                        node["title"]
                            ?.jsonObject
                            ?.get("runs")
                            ?.jsonArray
                            ?.getOrNull(0)
                            ?.jsonObject
                            ?.get("text")
                            ?.jsonPrimitive
                            ?.content
                            ?: "Unknown",

                    artist =
                        node["longBylineText"]
                            ?.jsonObject
                            ?.get("runs")
                            ?.jsonArray
                            ?.joinToString("") {
                                it.jsonObject["text"]
                                    ?.jsonPrimitive
                                    ?.content ?: ""
                            }
                            ?: "Unknown",

                    thumbnailUrl =
                        extractThumbnail(node)
                )

            } catch (_: Exception) {
                null
            }
        }
    }
}