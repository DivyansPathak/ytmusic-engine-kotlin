package model

data class AlbumSong(
    val videoId: String,
    val title: String,
    val artists: List<ArtistRef>,
    val duration: String?,
    val plays: String?,
    val trackNumber: Int?,
    val thumbnailUrl: String
)