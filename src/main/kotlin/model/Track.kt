package model

data class Track(

    val videoId: String,

    val title: String,

    val artists: List<ArtistRef>,

    val album: String?,

    val albumId: String,

    val duration: String?,

    val durationSeconds: Int?,

    val thumbnailUrl: String,

    val plays: String?,

    val trackNumber: Int?
)