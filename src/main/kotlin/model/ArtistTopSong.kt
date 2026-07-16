package model

data class ArtistTopSong(
    val videoId: String,
    val title: String,
    val artists: List<ArtistRef>,
    val album: String?,
    val albumId: String?,
    val plays: String?,
    val thumbnailUrl: String
)