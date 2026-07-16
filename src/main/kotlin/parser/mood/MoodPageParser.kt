package parser.mood

import kotlinx.serialization.json.JsonElement
import model.mood.MoodPage
import utils.findNodes

object MoodPageParser {

//    fun parse(
//        root: JsonElement,
//        title: String
//    ): MoodPage {
//
//        val shelves =
//            findNodes(
//                root,
//                "musicCarouselShelfRenderer"
//            ).map {
//                MoodShelfParser.parse(it)
//            }
//
//        return MoodPage(
//            title = title,
//            shelves = shelves
//        )
//    }

    fun parse(
        root: JsonElement,
        title: String
    ): MoodPage {

        val rawShelves =
            findNodes(
                root,
                "musicCarouselShelfRenderer"
            )

        rawShelves.forEach { shelf ->

            println("========== SHELF ==========")

            println(
                shelf["header"]
            )

            println(
                "TwoRow = ${
                    findNodes(
                        shelf,
                        "musicTwoRowItemRenderer"
                    ).size
                }"
            )

            println(
                "Responsive = ${
                    findNodes(
                        shelf,
                        "musicResponsiveListItemRenderer"
                    ).size
                }"
            )
        }

        val shelves =
            rawShelves.map {
                MoodShelfParser.parse(it)
            }

        return MoodPage(
            title = title,
            shelves = shelves
        )
    }
}