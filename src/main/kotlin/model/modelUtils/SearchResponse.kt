package model.modelUtils

import model.Song

data class SearchResponse(
    val songs: List<Song>
)