package wise.military.wisemilitarylife.model

data class User(
    val id: String = "",
    val password: String = "",
    val age: Int = 0,
    val level: Int = Level.One,
)
