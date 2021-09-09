package wise.military.life.util.extension

import android.app.Activity
import android.widget.Toast

fun Activity.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Activity.exceptionToast(what: String, exception: Exception) {
    toast(
        message = "${what}에 오류가 발생했어요 \uD83D\uDE13\n\n${exception.getErrorMessage()}",
        length = Toast.LENGTH_LONG
    )
}
