package model

data class ArtistPage(
    val header: ArtistHeader,
    val topSongs : List<Track>,
    val albums: List<AlbumPreview>,
    val singles: List<AlbumPreview>,
    val fansAlsoLike: List<ArtistPreview> = emptyList(),
    val videos: List<ArtistVideo> = emptyList()

)