package repository


import model.*
import model.charts.ChartPage
import model.explore.ExplorePage
import model.home.HomeFeed
import model.modelUtils.SearchFilter
import model.mood.Mood
import model.mood.MoodPage
import model.newrelease.NewReleasePage
import model.playlist.PlaylistPage
import network.InnerTubeClient
import parser.album.AlbumPageParser
import parser.artist.ArtistPageParser
import parser.artist.ArtistParser
import parser.charts.ChartPageParser
import parser.explore.ExplorePageParser
import parser.home.HomeFeedParser
import parser.mood.MoodPageParser
import parser.mood.MoodsGenresParser
import parser.newrelease.NewReleasePageParser
import parser.playlist.PlaylistPageParser
import parser.queue.QueueParser
import parser.search.SearchParser

class YoutubeMusicRepository(
    private val client: InnerTubeClient
) {

    suspend fun searchSongs(
        query: String
    ): List<Track> {

        return SearchParser.parse(
            client.search(
                query,
                SearchFilter.SONGS
            )
        )
    }
    suspend fun searchSuggestions(
        query: String
    ): List<String> {

        return client.searchSuggestions(
            query
        )
    }

    suspend fun searchArtists(
        query: String
    ): List<Artist> {

        return ArtistParser.parse(
            client.search(
                query,
                SearchFilter.ARTISTS
            )
        )
    }
    suspend fun artistPage(
        artistId: String
    ): ArtistPage {

        return ArtistPageParser.parse(
            client.browse(artistId),
            artistId
        )
    }

    suspend fun album(
        albumId: String
    ): AlbumPage {

        val response =
            client.browse(albumId)

        return AlbumPageParser.parse(
            response,
            albumId
        )
    }

    suspend fun playlist(
        playlistId: String
    ): PlaylistPage {

        val browseId =
            if (playlistId.startsWith("VL"))
                playlistId
            else
                "VL$playlistId"

        val response =
            client.browse(browseId)

        return PlaylistPageParser.parse(
            response,
            playlistId
        )
    }

    suspend fun home() : HomeFeed {
        return HomeFeedParser.parse(
            client.browse("FEmusic_home")
        )
    }

    suspend fun moodsAndGenres(
    ): List<Mood> {

        val response =
            client.browse(
                "FEmusic_moods_and_genres"
            )

        return MoodsGenresParser.parse(
            response
        )
    }
    suspend fun moodPage(
        mood: Mood
    ): MoodPage {

        val response =
            client.browse(
                browseId = mood.browseId,
                params = mood.params
            )

        return MoodPageParser.parse(
            response,
            mood.title
        )
    }
    suspend fun explore(): ExplorePage {

        return ExplorePageParser.parse(
            client.browse(
                "FEmusic_explore"
            )
        )
    }
    suspend fun trending(): PlaylistPage {

        return playlist(
            "VLOLAK5uy_lSTp1DIuzZBUyee3kDsXwPgP25WdfwB40"
        )
    }
    suspend fun newReleasesAlbums(
    ): NewReleasePage {

        return NewReleasePageParser.parse(
            client.browse(
                "FEmusic_new_releases_albums"
            )
        )
    }
    suspend fun charts(): ChartPage {

        return ChartPageParser.parse(
            client.browse(
                "FEmusic_charts"
            )
        )
    }
    suspend fun radio(
        videoId: String
    ): List<QueueItem> {

        return QueueParser.parse(
            client.next(videoId)
        )
    }

}