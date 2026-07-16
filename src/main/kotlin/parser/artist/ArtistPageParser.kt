package parser.artist

import kotlinx.serialization.json.JsonElement
import model.ArtistPage

object ArtistPageParser {

    fun parse(
        root: JsonElement,
        artistId: String
    ): ArtistPage {

        val header =
            ArtistHeaderParser.parse(
                root,
                artistId
            )

        val songs =
            ArtistTopSongsParser.parse(root)

        val albums =
            ArtistAlbumsParser.parse(root)

        val singles =
            ArtistSinglesParser.parse(root)

        val artistFansAlsoLike =
            ArtistFansAlsoLikeParser.parse(root)

        val videos =
            ArtistVideosParser.parse(root)

        return ArtistPage(
            header = header,
            topSongs = songs,
            albums = albums,
            singles = singles,
            fansAlsoLike = artistFansAlsoLike,
            videos = videos
        )
    }
}