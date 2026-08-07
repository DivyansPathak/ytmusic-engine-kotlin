package model.mood

data class Mood(
    val title: String,
    val browseId: String,
    val params: String?
)

data class MoodSection(
    val title: String,
    val moods: List<Mood>
)