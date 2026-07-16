package model.playlist


data class PlaylistHeader(
    val id: String,
    val title: String,
    val author: String?,
    val songCount: String?,
    val duration: String?,
    val thumbnailUrl: String,
    val description: String?
)