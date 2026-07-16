package model

data class ArtistHeader(
    val id: String,
    val name: String,
    val description: String?,
    val monthlyAudience: String?,
    val thumbnailUrl: String?,
    val channelUrl: String?
)