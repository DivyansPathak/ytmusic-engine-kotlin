package parser.charts

import kotlinx.serialization.json.JsonElement
import model.charts.ChartPage
import utils.findNodes

object ChartPageParser {

    fun parse(
        root: JsonElement
    ): ChartPage {

        val shelves =
            findNodes(
                root,
                "musicCarouselShelfRenderer"
            ).map {
                ChartShelfParser.parse(it)
            }

        return ChartPage(
            shelves = shelves
        )
    }
}