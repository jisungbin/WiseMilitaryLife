package wise.military.life.model

data class Vaccine(
    val userId: String = "",
    val type: Int = 0, // 백신 종류
    val count: Int = 0, // 접종 휫수
)

fun Int.toVaccineString() = when (this) {
    0 -> "아스트라제네카"
    1 -> "화이자"
    2 -> "모더나"
    3 -> "얀센"
    else -> throw IndexOutOfBoundsException()
}
