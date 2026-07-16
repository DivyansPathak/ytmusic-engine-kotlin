package model

data class AlbumPage(
    val header: AlbumHeader,
    val songs: List<Track>
)