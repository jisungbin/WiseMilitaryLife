package wise.military.life.model

data class Vaccine(
    val userId: String = "",
    val type: String = "", // 백신 종류
    val index: Int = 0, // 접종 휫수
)

object VaccineType {
    const val AstraZeneca = "아스트라제네카"
    const val Pfizer = "화이자"
    const val Moderna = "모더나"
    const val Janssen = "얀센"
}
