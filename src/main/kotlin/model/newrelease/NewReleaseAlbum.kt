package model.newrelease

import model.ArtistRef

data class NewReleaseAlbum(
    val id: String,
    val title: String,
    val artists: List<ArtistRef>,
    val thumbnailUrl: String
)