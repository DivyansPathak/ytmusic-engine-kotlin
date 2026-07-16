package model

data class ArtistVideo(
    val videoId: String,
    val title: String,
    val duration: String?,
    val artist: String?,
    val thumbnailUrl: String
)