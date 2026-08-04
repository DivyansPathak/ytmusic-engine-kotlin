package parser.lyrics

import model.lyrics.LyricsPage
import kotlinx.serialization.json.*


object LyricsParser {

    /**
     * Extracts the lyrics browseId (e.g., "MPLYt_...") from client.next(videoId) response.
     */
    fun parseBrowseId(json: JsonElement): String? {
        val root = json as? JsonObject ?: return null

        // Navigate tabs in watchNextTabbedResultsRenderer
        val tabs = root["contents"]
            ?.jsonObject
            ?.get("singleColumnMusicWatchNextResultsRenderer")
            ?.jsonObject
            ?.get("tabbedRenderer")
            ?.jsonObject
            ?.get("watchNextTabbedResultsRenderer")
            ?.jsonObject
            ?.get("tabs")
            ?.jsonArray ?: return null

        for (tab in tabs) {
            val endpoint = tab.jsonObject["tabRenderer"]
                ?.jsonObject
                ?.get("endpoint")
                ?.jsonObject
                ?.get("browseEndpoint")
                ?.jsonObject

            val pageType = endpoint
                ?.get("browseEndpointContextSupportedConfigs")
                ?.jsonObject
                ?.get("browseEndpointContextMusicConfig")
                ?.jsonObject
                ?.get("pageType")
                ?.jsonPrimitive
                ?.content

//            if (pageType == "MUSIC_PAGE_TYPE_LYRICS") {
//                return endpoint["browseId"]?.jsonPrimitive?.content
//            }

            if (
                pageType == "MUSIC_PAGE_TYPE_TRACK_LYRICS" ||
                pageType == "MUSIC_PAGE_TYPE_LYRICS"
            ) {
                return endpoint["browseId"]
                    ?.jsonPrimitive
                    ?.content
            }
        }
        return null
    }

    /**
     * Parses the response from client.browse(lyricsBrowseId) into a LyricsPage object.
     */
    fun parseLyrics(json: JsonElement): LyricsPage? {
        val root = json as? JsonObject ?: return null

        // Lyrics content usually sits in musicDescriptionShelfRenderer
        val sectionList = root["contents"]
            ?.jsonObject
            ?.get("sectionListRenderer")
            ?.jsonObject
            ?.get("contents")
            ?.jsonArray ?: return null

        val shelf = sectionList.firstOrNull()
            ?.jsonObject
            ?.get("musicDescriptionShelfRenderer")
            ?.jsonObject ?: return null

        // Extract plain text runs
        val runs = shelf["description"]
            ?.jsonObject
            ?.get("runs")
            ?.jsonArray ?: return null

        val lyricsBuilder = StringBuilder()
        for (run in runs) {
            val text = run.jsonObject["text"]?.jsonPrimitive?.content ?: ""
            lyricsBuilder.append(text)
        }

        // Extract copyright / provider footer
        val footerText = shelf["footer"]
            ?.jsonObject
            ?.get("runs")
            ?.jsonArray
            ?.firstOrNull()
            ?.jsonObject
            ?.get("text")
            ?.jsonPrimitive
            ?.content

        val resultText = lyricsBuilder.toString().trim()
        if (resultText.isEmpty()) return null

        return LyricsPage(
            lyrics = resultText,
            source = footerText
        )
    }
}