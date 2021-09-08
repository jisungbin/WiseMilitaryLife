package wise.military.life.util.extension

import wise.military.life.util.config.UserConfig

fun String.isAdminId() = this == UserConfig.AdminId
