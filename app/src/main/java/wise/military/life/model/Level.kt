package wise.military.life.model

object Level {
    const val One = 0 // 일병
    const val Two = 1 // 이병
    const val Three = 2 // 상병
    const val Four = 3 // 병장
}

fun Int.toLevelString() = when (this) {
    0 -> "일병"
    1 -> "이병"
    2 -> "상병"
    3 -> "병장"
    else -> throw IndexOutOfBoundsException()
}
