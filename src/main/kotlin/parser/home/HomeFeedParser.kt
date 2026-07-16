package parser.home

import kotlinx.serialization.json.JsonElement
import model.home.HomeFeed
import utils.findNodes

object HomeFeedParser {

    fun parse(
        root: JsonElement
    ): HomeFeed {

        val shelves =
            findNodes(
                root,
                "musicCarouselShelfRenderer"
            )

        return HomeFeed(
            shelves =
                shelves.map {
                    HomeShelfParser.parse(it)
                }
        )
    }
}