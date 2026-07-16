package model

data class AlbumHeader(
    val id: String,
    val title: String,
    val artists: List<ArtistRef>,
    val year: String?,
    val songCount: String?,
    val duration: String?,
    val thumbnailUrl: String,
    val description: String?
)