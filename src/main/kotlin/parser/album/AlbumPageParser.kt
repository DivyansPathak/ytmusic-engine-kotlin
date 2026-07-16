package parser.album

import kotlinx.serialization.json.JsonElement
import model.AlbumPage

object AlbumPageParser {

    fun parse(
        root: JsonElement,
        albumId: String
    ): AlbumPage {

        val header =
            AlbumHeaderParser.parse(
                root,
                albumId
            )

        val songs =
            AlbumSongsParser.parse(
                root
            )

        return AlbumPage(
            header = header,
            songs = songs
        )
    }
}