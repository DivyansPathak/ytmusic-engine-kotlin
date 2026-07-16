import kotlinx.coroutines.runBlocking
import network.InnerTubeClient
import repository.YoutubeMusicRepository

fun main() = runBlocking {

    val client = InnerTubeClient()

    val repository =
        YoutubeMusicRepository(client)

    println(
        "====================================="
    )
    println(
        "YouTube Music Parser Test Console"
    )
    println(
        "====================================="
    )

    // =========================================================
    // SEARCH SONGS
    // =========================================================

//    val songs =
//        repository.searchSongs(
//            "yu hi chala chal"
//        )
//
//    println(
//        "\n========== SEARCH SONGS =========="
//    )
//
//    songs.take(5).forEach {
//        println(it)
//    }
// =========================================================
// RADIO
// =========================================================

// val songs = repository.searchSongs("Believer")
//
// val queue = repository.radio(
//     songs.first().videoId
// )
//
// println("\n========== RADIO ==========")
//
// println(
//     "Tracks : ${queue.size}"
// )
//
// println(
//     queue.take(5)
// )
    // =========================================================
    // SEARCH ARTISTS
    // =========================================================

//    val artists =
//        repository.searchArtists(
//            "Eminem"
//        )
//
//    println(
//        "\n========== SEARCH ARTISTS =========="
//    )
//
//    artists.take(5).forEach {
//        println(it)
//    }

    // =========================================================
    // ARTIST PAGE
    // =========================================================

//    val artistPage =
//        repository.artistPage(
//            "UCDxKh1gFWeYsqePvgVzmPoQ"
//        )
//
//    println(
//        "\n========== ARTIST PAGE =========="
//    )
//
//    println(artistPage)
//
//    println(
//        "Top Songs : ${artistPage.topSongs.size}"
//    )
//
//    println(
//        "Albums : ${artistPage.albums.size}"
//    )
//
//    println(
//        "Singles : ${artistPage.singles.size}"
//    )
//
//    println(
//        "Fans Also Like : ${artistPage.fansAlsoLike.size}"
//    )
//
//    println(
//        "Videos : ${artistPage.videos.size}"
//    )

    // =========================================================
    // ALBUM PAGE
    // =========================================================

//    val album =
//        repository.album(
//            "MPREb_E4GfUXfDfhy"
//        )
//
//    println(
//        "\n========== ALBUM PAGE =========="
//    )
//
//    println(album.header)
//
//    println(
//        "\nSongs : ${album.songs.size}"
//    )
//
//    println(
//        album.songs.take(5)
//    )

    // =========================================================
    // PLAYLIST PAGE
    // =========================================================

//    val playlist =
//        repository.playlist(
//            "PLFgquLnL59alCl_2TQvOiD5Vgm1hCaGSI"
//        )
//
//    println(
//        "\n========== PLAYLIST PAGE =========="
//    )
//
//    println(
//        playlist.header
//    )
//
//    println(
//        "\nSongs : ${playlist.songs.size}"
//    )
//
//    println(
//        playlist.songs.take(5)
//    )

    // =========================================================
    // HOME FEED
    // =========================================================
//
//    val home =
//        repository.home()
//
//    println(
//        "\n========== HOME FEED =========="
//    )
//
//    println(
//        "Shelves : ${home.shelves.size}"
//    )
//
//    home.shelves.forEach {
//
//        println()
//        println(
//            "===== ${it.title} ====="
//        )
//
//        println(
//            it.items.take(3)
//        )
//    }

    // =========================================================
    // MOODS & GENRES LIST
    // =========================================================

//    val moods =
//        repository.moodsAndGenres()
//
//    println(
//        "\n========== MOODS & GENRES =========="
//    )
//
//    moods.forEachIndexed { index, mood ->
//
//        println(
//            "$index -> ${mood.title}"
//        )
//    }
//
//    println(
//        "\nTotal moods : ${moods.size}"
//    )

    // =========================================================
    // MOOD PAGE
    // =========================================================

//    val moods =
//        repository.moodsAndGenres()
//
//    val hindi =
//        moods.first {
//            it.title == "Hindi"
//        }
//
//    val page =
//        repository.moodPage(
//            hindi
//        )
//
//    println(
//        "\n========== MOOD PAGE =========="
//    )
//
//    println(
//        "Mood : ${page.title}"
//    )
//
//    println(
//        "Shelves : ${page.shelves.size}"
//    )
//
//    page.shelves.forEach {
//
//        println()
//        println(
//            "===== ${it.title} ====="
//        )
//
//        println(
//            "Items : ${it.items.size}"
//        )
//
//        println(
//            "Songs : ${it.songs.size}"
//        )
//
//        if (it.songs.isNotEmpty()) {
//
//            println(
//                it.songs.take(3)
//            )
//        }
//
//        if (it.items.isNotEmpty()) {
//
//            println(
//                it.items.take(3)
//            )
//        }
//    }

    // =========================================================
    // EXPLORE
    // =========================================================

//    val explore =
//        repository.explore()
//
//    println(
//        "\n========== EXPLORE =========="
//    )
//
//    println(
//        explore.sections
//    )

    // =========================================================
    // TRENDING
    // =========================================================

//    val trending =
//        repository.trending()
//
//    println(
//        "\n========== TRENDING =========="
//    )
//
//    println(
//        trending.header
//    )
//
//    println(
//        "Songs : ${trending.songs.size}"
//    )

    // =========================================================
    // NEW RELEASES
    // =========================================================

//    val releases =
//        repository.newReleasesAlbums()
//
//    println(
//        "\n========== NEW RELEASES =========="
//    )
//
//    println(
//        releases.albums.first()
//    )
//
//    println(
//        "Albums : ${releases.albums.size}"
//    )

    // =========================================================
    // Charts
    // =========================================================
//    val charts =
//        repository.charts()
//
//    println(
//        "Shelves : ${charts.shelves.size}"
//    )
//
//    charts.shelves.forEach {
//
//        println()
//        println("===== ${it.title} =====")
//
//        println(
//            "Playlists : ${it.playlists.size}"
//        )
//
//        println(
//            it.playlists.take(3)
//        )
//
//        println()
//
//        println(
//            "Artists : ${it.artists.size}"
//        )
//
//        println(
//            it.artists.take(3)
//        )
//    }

// =========================================================
// SEARCH SUGGESTIONS
// =========================================================

//    val suggestions =
//        repository.searchSuggestions(
//            "emi"
//        )
//
//    println(
//        "\n========== SEARCH SUGGESTIONS =========="
//    )
//
//    println(
//        "Suggestions : ${suggestions.size}"
//    )
//
//    suggestions.forEach {
//
//        println(it)
//    }


    client.close()
}