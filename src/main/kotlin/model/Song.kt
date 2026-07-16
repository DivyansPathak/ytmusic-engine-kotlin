package model

data class Song(
    val videoId: String,
    val title: String,
    val artists: List<ArtistRef>,
    val album: String?,
    val albumId:String,
    val duration: String?,
    val durationSeconds: Int?,
    val thumbnailUrl: String
)