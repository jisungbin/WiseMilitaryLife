package wise.military.life.model

import kotlin.random.Random

data class Notice(
    val id: Int = Random.nextInt(),
    val title: String = "",
    val content: String = "",
    val createAt: String = "",
)
