package parser.playlist


import kotlinx.serialization.json.JsonElement
import model.playlist.PlaylistPage
import parser.album.AlbumSongsParser

object PlaylistPageParser {

    fun parse(
        root: JsonElement,
        playlistId: String
    ): PlaylistPage {

        val header =
            PlaylistHeaderParser.parse(
                root,
                playlistId
            )

        val songs =
            PlaylistSongsParser.parse(
                root
            )

        return PlaylistPage(
            header = header,
            songs = songs
        )
    }
}