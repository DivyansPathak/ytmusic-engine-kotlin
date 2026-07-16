package model.playlist


import model.Track


data class PlaylistPage(
    val header: PlaylistHeader,
    val songs: List<Track>
)