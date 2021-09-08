package wise.military.life.util.extension

import android.content.Intent
import wise.military.life.util.config.IntentConfig

fun Intent.getUserId() = getStringExtra(IntentConfig.UserId)!!
