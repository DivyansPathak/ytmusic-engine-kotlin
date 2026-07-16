package model.charts

data class ChartShelf(
    val title: String,
    val playlists: List<ChartPlaylist>,
    val artists: List<ChartArtist>
)