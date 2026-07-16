package model.mood

import model.Song
import model.home.HomeItem

data class MoodShelf(
    val title: String,
    val items: List<HomeItem>,
    val songs: List<Song> = emptyList()
)