package utils

fun durationToSeconds(
    duration: String?
): Int? {

    if (duration == null)
        return null

    val parts = duration.split(":")

    return when(parts.size) {

        2 -> {
            parts[0].toInt() * 60 +
                    parts[1].toInt()
        }

        3 -> {
            parts[0].toInt() * 3600 +
                    parts[1].toInt() * 60 +
                    parts[2].toInt()
        }

        else -> null
    }
}